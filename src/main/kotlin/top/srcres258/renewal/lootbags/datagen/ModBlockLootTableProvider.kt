package top.srcres258.renewal.lootbags.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import top.srcres258.renewal.lootbags.block.ModBlocks

class ModBlockLootTableProvider(
    registries: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), registries) {
    override fun generate() {
        dropSelf(ModBlocks.LOOT_RECYCLER.get())
        dropSelf(ModBlocks.BAG_OPENER.get())
        dropSelf(ModBlocks.BAG_STORAGE.get())
    }

    override fun getKnownBlocks(): Iterable<Block> = Iterable {
        ModBlocks.BLOCKS.entries.map { it.value() }.iterator()
    }
}