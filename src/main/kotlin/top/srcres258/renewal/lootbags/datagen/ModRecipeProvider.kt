package top.srcres258.renewal.lootbags.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.conditions.IConditionBuilder
import top.srcres258.renewal.lootbags.block.ModBlocks
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, registries), IConditionBuilder {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.LOOT_RECYCLER)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("ACA")
            .define('A', Blocks.STONE)
            .define('B', Items.CHEST)
            .define('C', Items.IRON_INGOT)
            .unlockedBy("has_chest", has(Items.CHEST))
            .save(recipeOutput)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BAG_OPENER)
            .pattern("ACA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', Blocks.STONE)
            .define('B', Items.CHEST)
            .define('C', Items.IRON_INGOT)
            .unlockedBy("has_chest", has(Items.CHEST))
            .save(recipeOutput)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BAG_STORAGE)
            .pattern("AAA")
            .pattern("CBC")
            .pattern("AAA")
            .define('A', Blocks.STONE)
            .define('B', Items.CHEST)
            .define('C', Items.IRON_INGOT)
            .unlockedBy("has_chest", has(Items.CHEST))
            .save(recipeOutput)
    }
}