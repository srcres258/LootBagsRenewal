package top.srcres258.renewal.lootbags.item.custom

import net.minecraft.world.item.Item
import top.srcres258.renewal.lootbags.util.LootBagType

class LootBagItem(
    val type: LootBagType,
    properties: Properties = Properties().stacksTo(16)
) : Item(properties) {
}