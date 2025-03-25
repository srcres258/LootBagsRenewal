package top.srcres258.renewal.lootbags.block.entity.custom

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler
import top.srcres258.renewal.lootbags.block.entity.ModBlockEntities
import top.srcres258.renewal.lootbags.item.ModItems
import top.srcres258.renewal.lootbags.item.custom.LootBagItem
import top.srcres258.renewal.lootbags.item.custom.LootBagType
import top.srcres258.renewal.lootbags.item.custom.asLootBagType
import top.srcres258.renewal.lootbags.screen.custom.BagStorageMenu
import kotlin.math.min

class BagStorageBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(ModBlockEntities.BAG_STORAGE.get(), pos, blockState), MenuProvider {
    private inner class BagStorageItemHandler : ItemStackHandler(SLOTS_COUNT) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            when (slot) {
                INPUT_SLOT -> {
                    for (bagItem in ModItems.LOOT_BAGS) {
                        if (stack.item == bagItem.get()) {
                            return true
                        }
                    }
                    // Refuse to insert items which are not loot bags into the input slot.
                    return false
                }
                // Refuse to insert items into the output slot and other slots.
                else -> return false
            }
        }

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
            val resultStack = super.extractItem(slot, amount, simulate)
            if (slot == OUTPUT_SLOT) {
                storedBagAmount -= resultStack.count * targetBagType.amountFactorEquivalentTo(LootBagType.COMMON).toInt()
            }
            updateOutputSlot()

            return resultStack
        }

        override fun onContentsChanged(slot: Int) {
            if (slot != OUTPUT_SLOT) {
                // When slots that aren't the output slot are changed, update the output slot.
                updateOutputSlot()
            }
        }

        fun updateOutputSlot(setChanged: Boolean = true) {
            stacks[OUTPUT_SLOT] = ItemStack(targetBagType.asItem(), min(targetBagAmount, targetBagType.asItem().defaultMaxStackSize))
            if (setChanged) {
                setChangedAndUpdateBlock()
            }
        }
    }

    companion object {
        const val INPUT_SLOT = 0
        const val OUTPUT_SLOT = 1
        const val SLOTS_COUNT = OUTPUT_SLOT + 1
    }

    val itemHandler: ItemStackHandler = BagStorageItemHandler()

    private var storedBagAmount: Int = 0
    private var targetBagType: LootBagType = LootBagType.COMMON
    private val targetBagAmount: Int
        get() = (storedBagAmount.toFloat() * LootBagType.COMMON.amountFactorEquivalentTo(targetBagType)).toInt()
    private val data = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> storedBagAmount
            1 -> targetBagType.ordinal
            else -> 0
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> storedBagAmount = value
                1 -> {
                    targetBagType = LootBagType.entries[value]
                }
            }
        }

        override fun getCount(): Int = 2
    }

    override fun getDisplayName(): Component = Component.translatable("block.lootbags.bag_storage")

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
        BagStorageMenu(containerId, playerInventory, player.level(), this, data)

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.put("inventory", itemHandler.serializeNBT(registries))
        tag.putInt("stored_bag_amount", storedBagAmount)
        tag.putInt("target_bag_type", targetBagType.ordinal)

        super.saveAdditional(tag, registries)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"))
        storedBagAmount = tag.getInt("stored_bag_amount")
        val targetBagTypeOrdinal = tag.getInt("target_bag_type")
        if (targetBagTypeOrdinal in LootBagType.entries.indices) { // Make sure the ordinal is valid.
            targetBagType = LootBagType.entries[targetBagTypeOrdinal]
        }
    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        val inputStack = itemHandler.getStackInSlot(INPUT_SLOT).copy()
        val inputItem = inputStack.item
        if (!inputStack.isEmpty && inputItem is LootBagItem) {
            // If input is detected, consume it and increase storedBagAmount accordingly.
            itemHandler.extractItem(INPUT_SLOT, inputStack.count, false)
            storedBagAmount += (inputStack.count.toFloat() * inputItem.asLootBagType()
                .amountFactorEquivalentTo(LootBagType.COMMON)).toInt()
        }

        // Update the output slot every tick to keep everything valid. May bring some loss in performance,
        // but I don't know what to do. (networking within Minecraft's codebase is too complex to carry out
        // a triggering-based implementation on the update of block entity status, so I took the easiest
        // polling-based implementation.)
        (itemHandler as BagStorageItemHandler).updateOutputSlot()
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> =
        ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        saveWithoutMetadata(registries)

    private fun setChangedAndUpdateBlock() {
        setChanged()
        level?.let { level ->
            if (!level.isClientSide) {
                level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL)
            }
        }
    }
}