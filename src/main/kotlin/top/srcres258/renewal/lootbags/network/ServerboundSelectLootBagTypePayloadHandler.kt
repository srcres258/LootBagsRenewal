package top.srcres258.renewal.lootbags.network

import net.neoforged.neoforge.network.handling.IPayloadContext
import top.srcres258.renewal.lootbags.util.LootBagType
import top.srcres258.renewal.lootbags.network.custom.ServerboundSelectLootBagTypePayload
import top.srcres258.renewal.lootbags.screen.custom.BagStorageMenu

object ServerboundSelectLootBagTypePayloadHandler {
    fun handleDataOnMain(data: ServerboundSelectLootBagTypePayload, context: IPayloadContext) {
        val lootBagTypeIndex = data.lootBagTypeOrdinal % LootBagType.entries.size
        val menu = context.player().containerMenu
        if (menu is BagStorageMenu) {
            menu.targetBagType = LootBagType.entries[lootBagTypeIndex]
        }
    }
}