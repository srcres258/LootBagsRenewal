package top.srcres258.renewal.lootbags.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.conditions.IConditionBuilder
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.util.LootBagType
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    registries: HolderLookup.Provider,
    output: RecipeOutput
) : RecipeProvider(registries, output), IConditionBuilder {
    class Runner(
        packOutput: PackOutput,
        registries: CompletableFuture<HolderLookup.Provider>
    ) : RecipeProvider.Runner(packOutput, registries) {
        override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider =
            ModRecipeProvider(registries, output)

        override fun getName(): String = "${LootBags.MOD_ID} recipe provider"
    }

    override fun buildRecipes() {
        // Recipes for blocks with functionalities.
        shaped(RecipeCategory.MISC, ModBlocks.LOOT_RECYCLER)
            .pattern("AAA")
            .pattern("ABA")
            .pattern("ACA")
            .define('A', Blocks.STONE)
            .define('B', Items.CHEST)
            .define('C', Items.IRON_INGOT)
            .unlockedBy("has_chest", has(Items.CHEST))
            .save(output)
        shaped(RecipeCategory.MISC, ModBlocks.BAG_OPENER)
            .pattern("ACA")
            .pattern("ABA")
            .pattern("AAA")
            .define('A', Blocks.STONE)
            .define('B', Items.CHEST)
            .define('C', Items.IRON_INGOT)
            .unlockedBy("has_chest", has(Items.CHEST))
            .save(output)
        shaped(RecipeCategory.MISC, ModBlocks.BAG_STORAGE)
            .pattern("AAA")
            .pattern("CBC")
            .pattern("AAA")
            .define('A', Blocks.STONE)
            .define('B', Items.CHEST)
            .define('C', Items.IRON_INGOT)
            .unlockedBy("has_chest", has(Items.CHEST))
            .save(output)

        // Recipes for conversions between loot bags.
        for (type in LootBagType.entries) {
            for (otherType in LootBagType.entries) {
                // Skip for types larger than or equal to the target type.
                if (otherType.rarity >= type.rarity) {
                    continue
                }
                // Also skip for creative-only bags.
                if (type.creativeOnly || otherType.creativeOnly) {
                    continue
                }
                val amount = type.amountFactorEquivalentTo(otherType).toInt()
                // Skip for amounts larger than 4. Ingredients with massive amounts may cause
                // network errors during client-server communications.
                if (amount <= 4) {
                    shapeless(RecipeCategory.MISC, type.asItem())
                        .requires(otherType, type.amountFactorEquivalentTo(otherType).toInt())
                        .unlockedBy("has_${otherType.itemId}", has(otherType.asItem()))
                        .save(output, "${type.itemId}_from_${otherType.itemId}")
                }
            }
        }
    }
}