package top.srcres258.renewal.lootbags.screen.custom

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.SlotItemHandler

open class LootBagSlotItemHandler(
    itemHandler: IItemHandler,
    index: Int,
    xPosition: Int,
    yPosition: Int,
    val isOutputSlot: Boolean
) : SlotItemHandler(itemHandler, index, xPosition, yPosition) {
    fun extractItem(amount: Int, simulate: Boolean): ItemStack =
        itemHandler.extractItem(index, amount, simulate)
}