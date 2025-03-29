package top.srcres258.renewal.lootbags.block.entity.custom

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.util.Mth
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler
import top.srcres258.renewal.lootbags.block.entity.ModBlockEntities
import top.srcres258.renewal.lootbags.component.ModDataComponents
import top.srcres258.renewal.lootbags.screen.custom.LootRecyclerMenu
import top.srcres258.renewal.lootbags.util.*
import kotlin.math.min

class LootRecyclerBlockEntity(
    pos: BlockPos,
    blockState: BlockState
) : BlockEntity(ModBlockEntities.LOOT_RECYCLER.get(), pos, blockState), MenuProvider {
    enum class ContainerDataType {
        STORED_BAG_AMOUNT
    }

    private inner class LootRecyclerItemHandler : LootBagItemHandler(SLOTS_COUNT) {
        override fun isInputSlot(slot: Int): Boolean = slot == INPUT_SLOT

        // We want to recycle everything, so only check whether the slot is the input slot.
        // (i.e. Don't need to check whether the item is a loot bag.)
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean = isInputSlot(slot)

        // We want to recycle everything, so for the input slot unleash the maximum stack size.
        override fun getSlotLimit(slot: Int): Int = if (isInputSlot(slot)) {
            Item.ABSOLUTE_MAX_STACK_SIZE
        } else {
            super.getSlotLimit(slot)
        }

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
            if (simulate) { // NOTE to do simulation check!!!
                return super.extractItem(slot, amount, true)
            } else {
                val resultStack = super.extractItem(slot, amount, false)
                if (slot == OUTPUT_SLOT) {
                    storedBagAmount -= resultStack.count * targetBagType.amountFactorEquivalentTo(LootBagType.COMMON).toInt()
                }
                updateOutputSlot()

                return resultStack
            }
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

    val itemHandler: ItemStackHandler = LootRecyclerItemHandler()
    val inputItemHandler = InputOnlyItemHandler(itemHandler, INPUT_SLOT)
    val outputItemHandler = OutputOnlyItemHandler(itemHandler, OUTPUT_SLOT)

    var storedBagAmount: Int = 0
    /**
     * Currently `targetBagType` field is constant (no option to change it yet).
     */
    private val targetBagType: LootBagType = LootBagType.COMMON
    private val targetBagAmount: Int
        get() = (storedBagAmount.toFloat() * LootBagType.COMMON.amountFactorEquivalentTo(targetBagType)).toInt()
    private val data = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            ContainerDataType.STORED_BAG_AMOUNT.ordinal -> storedBagAmount
            else -> 0
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                ContainerDataType.STORED_BAG_AMOUNT.ordinal -> storedBagAmount = value
            }
        }

        override fun getCount(): Int = ContainerDataType.entries.size
    }

    private var accumulation = 0.0

    override fun getDisplayName(): Component = Component.translatable("block.lootbags.loot_recycler")

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
        LootRecyclerMenu(containerId, playerInventory, player.level(), this, data)

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.put("inventory", itemHandler.serializeNBT(registries))
        tag.putInt("stored_bag_amount", storedBagAmount)

        super.saveAdditional(tag, registries)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"))
        storedBagAmount = tag.getInt("stored_bag_amount")
    }

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        val inputStack = itemHandler.getStackInSlot(BagStorageBlockEntity.INPUT_SLOT).copy()
        if (!inputStack.isEmpty) {
            // If input is detected, consume (recycle) it and increase storedBagAmount accordingly.
            itemHandler.extractItem(BagStorageBlockEntity.INPUT_SLOT, inputStack.count, false)

            // Increase `accumulation` randomly.
            val random = level.random
            val rand = Mth.nextDouble(random, 0.0, 1.0)
            if (rand < 0.2) {
                val rand1 = Mth.nextDouble(random, 0.0, 0.1)
                accumulation += rand1 * inputStack.count
            }

            // If `accumulation` reached 1.0, we can increase `storedBagAmount` accordingly.
            if (accumulation >= 1.0) {
                val increment = accumulation.toInt()
                storedBagAmount += increment
                accumulation -= increment.toDouble()
            }
        }

        (itemHandler as LootRecyclerItemHandler).updateOutputSlot()
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> =
        ClientboundBlockEntityDataPacket.create(this)

    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag =
        saveWithoutMetadata(registries)

    private fun setChangedAndUpdateBlock() {
        setChangedAndUpdateBlock(level)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        // Adds the stored bags amount as a data component into the DataComponentMap builder.
        val bagStorage = BagStorageRecord(storedBagAmount, targetBagType.ordinal)
        components.set(ModDataComponents.BAG_STORAGE.get(), bagStorage)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        // Apply the stored bags data component if exists.
        componentInput.get(ModDataComponents.BAG_STORAGE.get())?.let { storedBags ->
            storedBagAmount = storedBags.storedBagAmount
        }
    }
}