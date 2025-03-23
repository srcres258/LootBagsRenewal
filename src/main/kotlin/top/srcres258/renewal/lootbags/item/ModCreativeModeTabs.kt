package top.srcres258.renewal.lootbags.item

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.block.ModBlocks
import java.util.function.Supplier

object ModCreativeModeTabs {
    val CREATIVE_MODE_TAB: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LootBags.MOD_ID)

    val LOOT_BAGS_TAB: Supplier<CreativeModeTab> = CREATIVE_MODE_TAB.register("loot_bags_tab") { ->
        CreativeModeTab.builder()
            .icon { ItemStack(ModItems.COMMON_LOOT_BAG.get()) }
            .title(Component.translatable("creativetab.lootbags.loot_bags_tab"))
            .displayItems { parameters, output ->
                output.accept(ModItems.COMMON_LOOT_BAG)
                output.accept(ModItems.UNCOMMON_LOOT_BAG)
                output.accept(ModItems.RARE_LOOT_BAG)
                output.accept(ModItems.EPIC_LOOT_BAG)
                output.accept(ModItems.LEGENDARY_LOOT_BAG)
                output.accept(ModItems.PATIENT_LOOT_BAG)
                output.accept(ModItems.ARTIFICIAL_LOOT_BAG)
                output.accept(ModItems.BACON_LOOT_BAG)

                output.accept(ModBlocks.LOOT_RECYCLER)
                output.accept(ModBlocks.BAG_OPENER)
                output.accept(ModBlocks.BAG_STORAGE)
            }
            .build()
    }

    fun register(eventBus: IEventBus) {
        CREATIVE_MODE_TAB.register(eventBus)
    }
}