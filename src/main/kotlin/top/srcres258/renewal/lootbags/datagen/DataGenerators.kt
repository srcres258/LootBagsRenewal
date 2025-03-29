package top.srcres258.renewal.lootbags.datagen

import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import top.srcres258.renewal.lootbags.LootBags

@EventBusSubscriber(modid = LootBags.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object DataGenerators {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        val generator = event.generator
        val packOutput = generator.packOutput
        val lookupProvider = event.lookupProvider

        generator.addProvider(true, LootTableProvider(packOutput, setOf(),
            listOf(LootTableProvider.SubProviderEntry(::ModBlockLootTableProvider, LootContextParamSets.BLOCK)),
            lookupProvider))
        generator.addProvider(true, ModRecipeProvider.Runner(packOutput, lookupProvider))

        val blockTagsProvider = ModBlockTagsProvider(packOutput, lookupProvider)
        val itemTagsProvider = ModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter())
        generator.addProvider(true, blockTagsProvider)
        generator.addProvider(true, itemTagsProvider)

        generator.addProvider(true, ModDataMapProvider(packOutput, lookupProvider))

        generator.addProvider(true, ModModelProvider(packOutput))

        generator.addProvider(true, ModDatapackProvider(packOutput, lookupProvider))
        generator.addProvider(true, ModGlobalLootModifierProvider(packOutput, lookupProvider))

    }
}