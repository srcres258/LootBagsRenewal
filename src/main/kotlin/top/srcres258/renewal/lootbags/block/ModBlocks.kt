package top.srcres258.renewal.lootbags.block

import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.renewal.lootbags.LootBags

object ModBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(LootBags.MOD_ID)

    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
    }
}
