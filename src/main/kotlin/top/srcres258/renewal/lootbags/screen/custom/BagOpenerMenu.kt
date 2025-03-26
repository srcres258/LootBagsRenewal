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
import top.srcres258.renewal.lootbags.block.entity.custom.BagOpenerBlockEntity
import top.srcres258.renewal.lootbags.screen.ModMenuTypes
import top.srcres258.renewal.lootbags.util.addPlayerHotbarSlots
import top.srcres258.renewal.lootbags.util.addPlayerInventorySlots
import top.srcres258.renewal.lootbags.util.quickMoveStack

// THIS YOU HAVE TO DEFINE!
private const val BE_INVENTORY_SLOT_COUNT = BagOpenerBlockEntity.SLOTS_COUNT

class BagOpenerMenu(
    containerId: Int,
    inv: Inventory,
    private val level: Level,
    val blockEntity: BagOpenerBlockEntity,
    val data: ContainerData
) : AbstractContainerMenu(ModMenuTypes.BAG_OPENER.get(), containerId) {
    constructor(
        containerId: Int,
        inv: Inventory,
        level: Level,
        extraData: FriendlyByteBuf
    ) : this(containerId, inv, level, level.getBlockEntity(extraData.readBlockPos()) as BagOpenerBlockEntity,
        SimpleContainerData(BagOpenerBlockEntity.ContainerDataType.entries.size))

    init {
        addPlayerInventorySlots(inv)
        addPlayerHotbarSlots(inv)

        addBlockEntityInputSlots()
        addBlockEntityOutputSlots()

        addDataSlots(data)
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack =
        quickMoveStack(this, player, index, BE_INVENTORY_SLOT_COUNT, ::moveItemStackTo).also {
            broadcastChanges()
        }

    override fun stillValid(player: Player): Boolean =
        stillValid(ContainerLevelAccess.create(level, blockEntity.blockPos), player, ModBlocks.BAG_OPENER.get())

    private fun addPlayerInventorySlots(inv: Inventory) {
        addPlayerInventorySlots(inv, 8, 102, ::addSlot)
    }

    private fun addPlayerHotbarSlots(inv: Inventory) {
        addPlayerHotbarSlots(inv, 8, 159, ::addSlot)
    }

    private fun addBlockEntityInputSlots() {
        val left = 8
        val top = 19
        for (i in 0 ..< 9) {
            addSlot(SlotItemHandler(blockEntity.inputItemHandler, i, left + i * 18, top))
        }
    }

    private fun addBlockEntityOutputSlots() {
        val left = 8
        val top = 43
        for (i in 0 ..< 3) {
            for (j in 0..< 9) {
                addSlot(SlotItemHandler(blockEntity.outputItemHandler, j + i * 9, left + j * 18, top + i * 18))
            }
        }
    }

    val isCrafting: Boolean
        get() = data.get(BagOpenerBlockEntity.ContainerDataType.PROGRESS.ordinal) > 0

    val scaledProgress: Int
        get() {
            val progress = data.get(BagOpenerBlockEntity.ContainerDataType.PROGRESS.ordinal)
            val maxProgress = data.get(BagOpenerBlockEntity.ContainerDataType.MAX_PROGRESS.ordinal)
            val progressBarPixelSize = 162

            return if (progress > 0 && maxProgress > 0) progress * progressBarPixelSize / maxProgress else 0
        }
}