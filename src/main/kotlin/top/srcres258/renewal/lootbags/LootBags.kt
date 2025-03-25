package top.srcres258.renewal.lootbags

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.block.entity.ModBlockEntities
import top.srcres258.renewal.lootbags.item.ModCreativeModeTabs
import top.srcres258.renewal.lootbags.item.ModItems
import top.srcres258.renewal.lootbags.network.ModNetworks
import top.srcres258.renewal.lootbags.screen.ModMenuTypes
import top.srcres258.renewal.lootbags.screen.custom.BagStorageScreen

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

        ModCreativeModeTabs.register(MOD_BUS)
        ModItems.register(MOD_BUS)
        ModBlocks.register(MOD_BUS)
        ModBlockEntities.register(MOD_BUS)
        ModMenuTypes.register(MOD_BUS)
    }

    fun registerPayloadHandlers(event: RegisterPayloadHandlersEvent) {
        ModNetworks.registerPayloadHandlers(event.registrar("1"))
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
    object ClientModEvents {
        @SubscribeEvent
        fun onRegisterScreens(event: RegisterMenuScreensEvent) {
            event.register(ModMenuTypes.BAG_STORAGE.get(), ::BagStorageScreen)
        }
    }
}