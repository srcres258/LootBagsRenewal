package top.srcres258.renewal.lootbags.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.component.ModDataComponents

class ModBlockLootTableProvider(
    registries: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), registries) {
    override fun generate() {
        dropSelf(ModBlocks.LOOT_RECYCLER.get())
        dropSelf(ModBlocks.BAG_OPENER.get())
        add(ModBlocks.BAG_STORAGE.get()) { block ->
            LootTable.lootTable()
                .withPool(applyExplosionDecay(
                    block,
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1F))
                        .add(
                            LootItem.lootTableItem(block)
                                .apply(
                                    CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                        .include(ModDataComponents.BAG_STORAGE.get())
                                )
                        )
                ))
        }
    }

    override fun getKnownBlocks(): Iterable<Block> = Iterable {
        ModBlocks.BLOCKS.entries.map { it.value() }.iterator()
    }
}