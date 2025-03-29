package top.srcres258.renewal.lootbags.util

import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.screen.custom.LootBagSlotItemHandler

// CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
// must assign a slot number to each of the slots used by the GUI.
// For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
// Each time we add a Slot to the container, it automatically increases the slotIndex, which means
//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
//  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
private const val HOTBAR_SLOT_COUNT = 9
private const val PLAYER_INVENTORY_ROW_COUNT = 3
private const val PLAYER_INVENTORY_COLUMN_COUNT = 9
private const val PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_ROW_COUNT * PLAYER_INVENTORY_COLUMN_COUNT
private const val VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT
private const val VANILLA_FIRST_SLOT_INDEX = 0
private const val BE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT

/**
 * NOTE: This function **assumes** that the beginning indices of slots are supposed to be **vanilla** slots!
 * Ensure your slots' indices' order before calling this function.
 */
fun quickMoveStack(
    menu: AbstractContainerMenu,
    player: Player,
    index: Int,
    beInventorySlotCount: Int,
    moveItemStackTo: (ItemStack, Int, Int, Boolean) -> Boolean
): ItemStack {
    val sourceSlot = menu.slots[index]
    if (!sourceSlot.hasItem()) {
        return ItemStack.EMPTY
    }
    val sourceStackOld = sourceSlot.item.copy()
    val sourceStack = sourceStackOld.copy()
    if (sourceStack.isEmpty) {
        return ItemStack.EMPTY
    }
    val copyOfSourceStack = sourceStack.copy()

    // Check if the slot clicked is one of the vanilla container slots
    if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
        // This is a vanilla container slot so merge the stack into the tile inventory
        if (!moveItemStackTo(sourceStack, BE_INVENTORY_FIRST_SLOT_INDEX,
                BE_INVENTORY_FIRST_SLOT_INDEX + beInventorySlotCount, false)) {
            return ItemStack.EMPTY
        }
    } else if (index < BE_INVENTORY_FIRST_SLOT_INDEX + beInventorySlotCount) {
        // This is a TE slot so merge the stack into the players inventory
        if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX,
                VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
            return ItemStack.EMPTY
        }
    } else {
        LootBags.LOGGER.warn("Invalid slot index: {}", index)
        return ItemStack.EMPTY
    }

    // If stack size == 0 (the entire stack was moved) set slot contents to null
    if (sourceStack.isEmpty) {
        if (sourceSlot is LootBagSlotItemHandler) {
            sourceSlot.extractItem(sourceStackOld.count, false)
        } else {
            sourceSlot.set(ItemStack.EMPTY)
        }
    } else {
        if (sourceSlot is LootBagSlotItemHandler) {
            val deltaCount = sourceStackOld.count - sourceStack.count
            if (deltaCount > 0) {
                sourceSlot.extractItem(deltaCount, false)
            }
        }
        sourceSlot.setChanged()
    }

    sourceSlot.onTake(player, sourceStack)
    return copyOfSourceStack
}

fun addPlayerInventorySlots(inv: Inventory, left: Int, top: Int, addSlot: (Slot) -> Slot) {
    for (i in 0 ..< 3) {
        for (j in 0 ..< 9) {
            addSlot(Slot(inv, j + i * 9 + 9, left + j * 18, top + i * 18))
        }
    }
}

fun addPlayerHotbarSlots(inv: Inventory, left: Int, top: Int, addSlot: (Slot) -> Slot) {
    for (i in 0 ..< 9) {
        addSlot(Slot(inv, i, left + i * 18, top))
    }
}