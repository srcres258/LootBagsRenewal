package top.srcres258.renewal.lootbags.item.custom

import net.minecraft.world.item.Item

class LootBagItem(
    val type: LootBagType,
    properties: Properties = Properties().stacksTo(16)
) : Item(properties) {
}