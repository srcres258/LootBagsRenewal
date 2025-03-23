package top.srcres258.renewal.lootbags.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.srcres258.renewal.lootbags.LootBags

class ModBlockStateProvider(
    output: PackOutput,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(output, LootBags.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {}
}