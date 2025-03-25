package top.srcres258.renewal.lootbags.component

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.util.BagStorageRecord
import java.util.function.Supplier

object ModDataComponents {
    val DATA_COMPONENTS: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, LootBags.MOD_ID)

    val BAG_STORAGE: Supplier<DataComponentType<BagStorageRecord>> =
        DATA_COMPONENTS.registerComponentType("bag_storage") { builder ->
            builder.persistent(BagStorageRecord.CODEC)
                .networkSynchronized(BagStorageRecord.STREAM_CODEC)
        }

    fun register(eventBus: IEventBus) {
        DATA_COMPONENTS.register(eventBus)
    }
}