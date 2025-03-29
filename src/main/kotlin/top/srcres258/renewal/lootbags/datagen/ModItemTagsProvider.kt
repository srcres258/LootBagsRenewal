package top.srcres258.renewal.lootbags.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.world.level.block.Block
import top.srcres258.renewal.lootbags.LootBags
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>
) : ItemTagsProvider(output, lookupProvider, blockTags, LootBags.MOD_ID) {
    override fun addTags(provider: HolderLookup.Provider) {}
}