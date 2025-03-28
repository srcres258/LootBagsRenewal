package top.srcres258.renewal.lootbags

import net.minecraft.core.Direction
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.block.entity.ModBlockEntities
import top.srcres258.renewal.lootbags.component.ModDataComponents
import top.srcres258.renewal.lootbags.item.ModCreativeModeTabs
import top.srcres258.renewal.lootbags.item.ModItems
import top.srcres258.renewal.lootbags.network.ModNetworks
import top.srcres258.renewal.lootbags.screen.ModMenuTypes
import top.srcres258.renewal.lootbags.screen.custom.BagOpenerScreen
import top.srcres258.renewal.lootbags.screen.custom.BagStorageScreen
import top.srcres258.renewal.lootbags.screen.custom.LootRecyclerScreen

/**
 * Main mod class. Should be an `object` declaration annotated with `@Mod`.
 * The modid should be declared in this object and should match the modId entry
 * in neoforge.mods.toml.
 *
 * An example for blocks is in the `blocks` package of this mod.
 */
@Mod(LootBags.MOD_ID)
object LootBags {
    const val MOD_ID = "lootbags"

    val LOGGER: Logger = LogManager.getLogger(MOD_ID)

    init {
        LOGGER.info("$MOD_ID is loading...")

        MOD_BUS.addListener(::registerPayloadHandlers)
        MOD_BUS.addListener(::registerCapabilities)

        ModCreativeModeTabs.register(MOD_BUS)
        ModItems.register(MOD_BUS)
        ModBlocks.register(MOD_BUS)
        ModBlockEntities.register(MOD_BUS)
        ModMenuTypes.register(MOD_BUS)
        ModDataComponents.register(MOD_BUS)
    }

    fun registerPayloadHandlers(event: RegisterPayloadHandlersEvent) {
        ModNetworks.registerPayloadHandlers(event.registrar("1"))
    }

    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.BAG_STORAGE.get(),
        ) { blockEntity, side ->
            when (side) {
                Direction.DOWN -> blockEntity.outputItemHandler
                else -> blockEntity.inputItemHandler
            }
        }
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.BAG_OPENER.get(),
        ) { blockEntity, side ->
            when (side) {
                Direction.DOWN -> blockEntity.outputItemHandler
                else -> blockEntity.inputItemHandler
            }
        }
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
    object ClientModEvents {
        @SubscribeEvent
        fun onRegisterScreens(event: RegisterMenuScreensEvent) {
            event.register(ModMenuTypes.BAG_STORAGE.get(), ::BagStorageScreen)
            event.register(ModMenuTypes.BAG_OPENER.get(), ::BagOpenerScreen)
            event.register(ModMenuTypes.LOOT_RECYCLER.get(), ::LootRecyclerScreen)
        }
    }
}