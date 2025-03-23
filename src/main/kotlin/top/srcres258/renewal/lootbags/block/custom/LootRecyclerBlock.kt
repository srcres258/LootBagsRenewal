package top.srcres258.renewal.lootbags.block.custom

import net.minecraft.world.level.block.Block

class LootRecyclerBlock(
    properties: Properties = Properties.of()
        .strength(3.5F)
        .requiresCorrectToolForDrops()
) : Block(properties) {
}