package top.srcres258.renewal.lootbags.network

import net.neoforged.neoforge.network.registration.PayloadRegistrar
import top.srcres258.renewal.lootbags.network.custom.ServerboundSelectLootBagTypePayload

object ModNetworks {
    fun registerPayloadHandlers(registrar: PayloadRegistrar) {
        registrar.playToServer(
            ServerboundSelectLootBagTypePayload.TYPE,
            ServerboundSelectLootBagTypePayload.STREAM_CODEC,
            ServerboundSelectLootBagTypePayloadHandler::handleDataOnMain
        )
    }
}