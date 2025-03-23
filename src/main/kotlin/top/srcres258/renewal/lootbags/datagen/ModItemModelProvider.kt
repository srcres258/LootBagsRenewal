package top.srcres258.renewal.lootbags.datagen

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.item.ModItems

class ModItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, LootBags.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        basicItem(ModItems.COMMON_LOOT_BAG.get())
        basicItem(ModItems.UNCOMMON_LOOT_BAG.get())
        basicItem(ModItems.RARE_LOOT_BAG.get())
        basicItem(ModItems.EPIC_LOOT_BAG.get())
        basicItem(ModItems.LEGENDARY_LOOT_BAG.get())
        basicItem(ModItems.PATIENT_LOOT_BAG.get())
        basicItem(ModItems.ARTIFICIAL_LOOT_BAG.get())
        basicItem(ModItems.BACON_LOOT_BAG.get())
    }
}