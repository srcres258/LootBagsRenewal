package top.srcres258.renewal.lootbags.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.block.ModBlocks
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : BlockTagsProvider(output, lookupProvider, LootBags.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.LOOT_RECYCLER.get())
            .add(ModBlocks.BAG_OPENER.get())
            .add(ModBlocks.BAG_STORAGE.get())
    }
}