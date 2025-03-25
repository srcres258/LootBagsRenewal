package top.srcres258.renewal.lootbags.component

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ExtraCodecs
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.renewal.lootbags.LootBags
import java.util.function.Supplier

object ModDataComponents {
    val DATA_COMPONENTS: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, LootBags.MOD_ID)

    val BAGS_STORED: Supplier<DataComponentType<Int>> =
        DATA_COMPONENTS.registerComponentType("bags_stored") { builder ->
            builder.persistent(ExtraCodecs.NON_NEGATIVE_INT)
                .networkSynchronized(ByteBufCodecs.VAR_INT)
        }

    fun register(eventBus: IEventBus) {
        DATA_COMPONENTS.register(eventBus)
    }
}