package top.srcres258.renewal.lootbags.screen.custom

import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.SlotItemHandler

open class BagStorageSlotItemHandler(
    itemHandler: IItemHandler,
    index: Int,
    xPosition: Int,
    yPosition: Int,
    val isOutputSlot: Boolean
) : SlotItemHandler(itemHandler, index, xPosition, yPosition)