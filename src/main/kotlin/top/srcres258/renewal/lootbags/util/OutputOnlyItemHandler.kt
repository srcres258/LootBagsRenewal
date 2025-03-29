package top.srcres258.renewal.lootbags.util

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class OutputOnlyItemHandler(
    private val itemHandler: IItemHandler,
    private val outputSlot: Int
) : IItemHandler {
    override fun getSlots(): Int = 1

    override fun getStackInSlot(slot: Int): ItemStack = itemHandler.getStackInSlot(outputSlot)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = stack.copy()

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack =
        itemHandler.extractItem(outputSlot, amount, simulate)

    override fun getSlotLimit(slot: Int): Int = itemHandler.getSlotLimit(outputSlot)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean =
        itemHandler.isItemValid(outputSlot, stack)
}