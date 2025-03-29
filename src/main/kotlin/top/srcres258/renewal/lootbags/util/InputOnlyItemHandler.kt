package top.srcres258.renewal.lootbags.util

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class InputOnlyItemHandler(
    private val itemHandler: IItemHandler,
    private val inputSlot: Int
) : IItemHandler {
    override fun getSlots(): Int = 1

    override fun getStackInSlot(slot: Int): ItemStack = ItemStack.EMPTY

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack =
        itemHandler.insertItem(inputSlot, stack, simulate)

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY

    override fun getSlotLimit(slot: Int): Int = itemHandler.getSlotLimit(inputSlot)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean =
        itemHandler.isItemValid(inputSlot, stack)
}