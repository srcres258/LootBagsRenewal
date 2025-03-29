package top.srcres258.renewal.lootbags.datagen

import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.item.ModItems
import java.util.stream.Stream

class ModModelProvider(
    output: PackOutput
) : ModelProvider(output, LootBags.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        registerBlockModels(blockModels)
        registerItemModels(itemModels)
    }

    override fun getKnownBlocks(): Stream<out Holder<Block>> =
        listOf(
            ModBlocks.LOOT_RECYCLER,
            ModBlocks.BAG_OPENER,
            ModBlocks.BAG_STORAGE
        ).stream()

    override fun getKnownItems(): Stream<out Holder<Item>> =
        listOf(
            ModItems.COMMON_LOOT_BAG,
            ModItems.UNCOMMON_LOOT_BAG,
            ModItems.RARE_LOOT_BAG,
            ModItems.EPIC_LOOT_BAG,
            ModItems.LEGENDARY_LOOT_BAG,
            ModItems.PATIENT_LOOT_BAG,
            ModItems.ARTIFICIAL_LOOT_BAG,
            ModItems.BACON_LOOT_BAG
        ).stream()
}

private fun registerBlockModels(blockModels: BlockModelGenerators) {
    simpleBlockWithItem(blockModels, ModBlocks.LOOT_RECYCLER)
    simpleBlockWithItem(blockModels, ModBlocks.BAG_OPENER)
    simpleBlockWithItem(blockModels, ModBlocks.BAG_STORAGE)
}

private fun registerItemModels(itemModels: ItemModelGenerators) {
    itemModels.generateFlatItem(ModItems.COMMON_LOOT_BAG.get(), ModelTemplates.FLAT_ITEM)
    itemModels.generateFlatItem(ModItems.UNCOMMON_LOOT_BAG.get(), ModelTemplates.FLAT_ITEM)
    itemModels.generateFlatItem(ModItems.RARE_LOOT_BAG.get(), ModelTemplates.FLAT_ITEM)
    itemModels.generateFlatItem(ModItems.EPIC_LOOT_BAG.get(), ModelTemplates.FLAT_ITEM)
    itemModels.generateFlatItem(ModItems.LEGENDARY_LOOT_BAG.get(), ModelTemplates.FLAT_ITEM)
    itemModels.generateFlatItem(ModItems.PATIENT_LOOT_BAG.get(), ModelTemplates.FLAT_ITEM)
    itemModels.generateFlatItem(ModItems.ARTIFICIAL_LOOT_BAG.get(), ModelTemplates.FLAT_ITEM)
    itemModels.generateFlatItem(ModItems.BACON_LOOT_BAG.get(), ModelTemplates.FLAT_ITEM)
}

private fun simpleBlockWithItem(blockModels: BlockModelGenerators, block: DeferredBlock<out Block>) {
    blockModels.createTrivialCube(block.get())
    blockModels.registerSimpleItemModel(block.get(), ResourceLocation.fromNamespaceAndPath(block.id.namespace, "block/${block.id.path}"))
}