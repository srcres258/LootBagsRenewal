package top.srcres258.renewal.lootbags.block.entity.custom

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler
import top.srcres258.renewal.lootbags.item.ModItems
import top.srcres258.renewal.lootbags.util.LootBagType

abstract class LootBagItemHandler(size: Int) : ItemStackHandler(size) {
    abstract fun isInputSlot(slot: Int): Boolean

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        if (isInputSlot(slot)) {
            for (bagItem in ModItems.LOOT_BAGS) {
                if (stack.item == bagItem.get()) {
                    return true
                }
            }
            // Refuse to insert items which are not loot bags into the input slot.
            return false
        } else {
            // Refuse to insert items into the output slot and other slots.
            return false
        }
    }

    override fun getSlotLimit(slot: Int): Int = LootBagType.COMMON.asItem().defaultMaxStackSize
}