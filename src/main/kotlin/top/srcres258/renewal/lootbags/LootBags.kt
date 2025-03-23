package top.srcres258.renewal.lootbags

import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.item.ModCreativeModeTabs
import top.srcres258.renewal.lootbags.item.ModItems

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

        ModCreativeModeTabs.register(MOD_BUS)
        ModItems.register(MOD_BUS)
        ModBlocks.register(MOD_BUS)
    }
}