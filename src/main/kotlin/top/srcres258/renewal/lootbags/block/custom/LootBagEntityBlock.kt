package top.srcres258.renewal.lootbags.block.custom

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.BaseEntityBlock
import top.srcres258.renewal.lootbags.component.ModDataComponents
import top.srcres258.renewal.lootbags.util.LootBagType

abstract class LootBagEntityBlock(properties: Properties) : BaseEntityBlock(properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: Item.TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        val bagStorage = stack.get(ModDataComponents.BAG_STORAGE.get())
        if (bagStorage != null) {
            if (tooltipFlag.hasShiftDown()) {
                tooltipComponents.add(
                    Component.translatable(
                    "tooltip.lootbags.bag_storage.stored_with_count",
                    bagStorage.storedBagAmount
                ))
                tooltipComponents.add(
                    Component.translatable(
                    "tooltip.lootbags.bag_storage.output_type",
                    LootBagType.entries[bagStorage.targetBagTypeOrdinal % LootBagType.entries.size]
                        .asItem().description.string
                ))
            } else {
                tooltipComponents.add(Component.translatable("tooltip.lootbags.press_shift_for_details"))
            }
        }
    }
}