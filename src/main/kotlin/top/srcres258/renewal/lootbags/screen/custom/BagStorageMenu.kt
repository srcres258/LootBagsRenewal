package top.srcres258.renewal.lootbags.screen.custom

import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.items.SlotItemHandler
import net.neoforged.neoforge.network.PacketDistributor
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.block.entity.custom.BagStorageBlockEntity
import top.srcres258.renewal.lootbags.util.LootBagType
import top.srcres258.renewal.lootbags.network.custom.ServerboundSelectLootBagTypePayload
import top.srcres258.renewal.lootbags.screen.ModMenuTypes
import top.srcres258.renewal.lootbags.util.ContainerMenuHelper

// THIS YOU HAVE TO DEFINE!
private const val BE_INVENTORY_SLOT_COUNT = BagStorageBlockEntity.SLOTS_COUNT // must be the number of slots you have!

class BagStorageMenu(
    containerId: Int,
    inv: Inventory,
    private val level: Level,
    val blockEntity: BagStorageBlockEntity,
    val data: ContainerData
) : AbstractContainerMenu(ModMenuTypes.BAG_STORAGE.get(), containerId) {
    constructor(
        containerId: Int,
        inv: Inventory,
        level: Level,
        extraData: FriendlyByteBuf
    ) : this(containerId, inv, level, level.getBlockEntity(extraData.readBlockPos()) as BagStorageBlockEntity,
        SimpleContainerData(2))

    init {
        addPlayerInventory(inv)
        addPlayerHotbar(inv)

        addSlot(SlotItemHandler(blockEntity.itemHandler, 0, 26, 16))
        addSlot(object : BagStorageSlotItemHandler(blockEntity.itemHandler, 1, 135, 16, true) {
            override fun getItem(): ItemStack {
                return if (targetBagAmount == 0) {
                    // When no target bag can be obtained, return one fake ItemStack to represent
                    // the current target bag type.
                    ItemStack(targetBagType.asItem())
                } else {
                    super.getItem()
                }
            }

            // Prevent the slot from being picked up when there is no target bag to be obtained (i.e.
            // when the fake ItemStack is displayed).
            override fun mayPickup(playerIn: Player): Boolean = targetBagAmount > 0

            // When there is no target bag to be obtained, this slot is considered "fake" and
            // should not be counted as a valid slot for the purpose of the player's inventory.
            override fun isFake(): Boolean = targetBagAmount == 0
        })

        addDataSlots(data)

        setSynchronizer(object : ContainerSynchronizer {
            override fun sendInitialData(
                container: AbstractContainerMenu,
                items: NonNullList<ItemStack>,
                carriedItem: ItemStack,
                initialData: IntArray
            ) {}

            override fun sendSlotChange(container: AbstractContainerMenu, slot: Int, itemStack: ItemStack) {}

            override fun sendCarriedChange(containerMenu: AbstractContainerMenu, stack: ItemStack) {}

            override fun sendDataChange(container: AbstractContainerMenu, id: Int, value: Int) {
                if (id == BagStorageBlockEntity.OUTPUT_SLOT && level.isClientSide) {
                    // When the output slot changes, and it's on the client, send it to the server to update the target bag type.
                    PacketDistributor.sendToServer(ServerboundSelectLootBagTypePayload(value))
                }
            }
        })
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack =
        ContainerMenuHelper.quickMoveStack(this, player, index, BE_INVENTORY_SLOT_COUNT, ::moveItemStackTo).also {
            broadcastChanges()
        }

    override fun stillValid(player: Player): Boolean =
        stillValid(ContainerLevelAccess.create(level, blockEntity.blockPos), player, ModBlocks.BAG_STORAGE.get())

    private fun addPlayerInventory(inv: Inventory) {
        val left = 8
        val top = 66
        for (i in 0 ..< 3) {
            for (j in 0 ..< 9) {
                addSlot(Slot(inv, j + i * 9 + 9, left + j * 18, top + i * 18))
            }
        }
    }

    private fun addPlayerHotbar(inv: Inventory) {
        val left = 8
        val top = 123
        for (i in 0 ..< 9) {
            addSlot(Slot(inv, i, left + i * 18, top))
        }
    }

    val storedBagAmount: Int
        get() = data.get(0)

    var targetBagType: LootBagType
        get() = LootBagType.entries[data.get(1)]
        set(value) {
            data.set(1, value.ordinal)
            broadcastChanges()
        }

    val targetBagAmount: Int
        get() = (data.get(0).toFloat() * LootBagType.COMMON.amountFactorEquivalentTo(
            LootBagType.entries[data.get(1)])).toInt()
}