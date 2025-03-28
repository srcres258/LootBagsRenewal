package top.srcres258.renewal.lootbags.screen.custom

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.items.SlotItemHandler
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.block.entity.custom.LootRecyclerBlockEntity
import top.srcres258.renewal.lootbags.screen.ModMenuTypes
import top.srcres258.renewal.lootbags.util.LootBagType
import top.srcres258.renewal.lootbags.util.addPlayerHotbarSlots
import top.srcres258.renewal.lootbags.util.addPlayerInventorySlots
import top.srcres258.renewal.lootbags.util.quickMoveStack

// THIS YOU HAVE TO DEFINE!
private const val BE_INVENTORY_SLOT_COUNT = LootRecyclerBlockEntity.SLOTS_COUNT // must be the number of slots you have!

class LootRecyclerMenu(
    containerId: Int,
    inv: Inventory,
    private val level: Level,
    val blockEntity: LootRecyclerBlockEntity,
    val data: ContainerData
) : AbstractContainerMenu(ModMenuTypes.LOOT_RECYCLER.get(), containerId) {
    constructor(
        containerId: Int,
        inv: Inventory,
        level: Level,
        extraData: FriendlyByteBuf
    ) : this(containerId, inv, level, level.getBlockEntity(extraData.readBlockPos()) as LootRecyclerBlockEntity,
        SimpleContainerData(LootRecyclerBlockEntity.ContainerDataType.entries.size))

    init {
        addPlayerInventorySlots(inv)
        addPlayerHotbarSlots(inv)

        addSlot(SlotItemHandler(blockEntity.itemHandler, 0, 44, 15))
        addSlot(object : BagStorageSlotItemHandler(blockEntity.itemHandler, 1, 116, 15, true) {
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
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack =
        quickMoveStack(this, player, index, BE_INVENTORY_SLOT_COUNT, ::moveItemStackTo).also {
            broadcastChanges()
        }

    override fun stillValid(player: Player): Boolean =
        stillValid(ContainerLevelAccess.create(level, blockEntity.blockPos), player, ModBlocks.LOOT_RECYCLER.get())

    private fun addPlayerInventorySlots(inv: Inventory) {
        addPlayerInventorySlots(inv, 8, 50, ::addSlot)
    }

    private fun addPlayerHotbarSlots(inv: Inventory) {
        addPlayerHotbarSlots(inv, 8, 107, ::addSlot)
    }

    val storedBagAmount: Int
        get() = data.get(LootRecyclerBlockEntity.ContainerDataType.STORED_BAG_AMOUNT.ordinal)

    val targetBagType: LootBagType = LootBagType.COMMON

    val targetBagAmount: Int
        get() = (storedBagAmount.toFloat() * LootBagType.COMMON.amountFactorEquivalentTo(targetBagType)).toInt()

}