package top.srcres258.renewal.lootbags.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import top.srcres258.renewal.lootbags.LootBags
import java.util.concurrent.CompletableFuture

class ModGlobalLootModifierProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : GlobalLootModifierProvider(output, registries, LootBags.MOD_ID) {
    override fun start() {}
}