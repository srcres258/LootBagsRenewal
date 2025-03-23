package top.srcres258.renewal.lootbags.datagen

import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.block.ModBlocks

class ModBlockStateProvider(
    output: PackOutput,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(output, LootBags.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        blockWithItem(ModBlocks.LOOT_RECYCLER.get())
        blockWithItem(ModBlocks.BAG_OPENER.get())
        blockWithItem(ModBlocks.BAG_STORAGE.get())
    }

    private fun blockWithItem(block: Block) {
        simpleBlockWithItem(block, cubeAll(block))
    }
}