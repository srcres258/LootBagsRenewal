package top.srcres258.renewal.lootbags.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeProvider
import net.neoforged.neoforge.common.conditions.IConditionBuilder
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, registries), IConditionBuilder {
}