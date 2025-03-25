package top.srcres258.renewal.lootbags.item

import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.item.custom.LootBagItem
import top.srcres258.renewal.lootbags.util.LootBagType

object ModItems {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(LootBags.MOD_ID)

    // loot bag items
    val COMMON_LOOT_BAG: DeferredItem<Item> = registerLootBagItem(LootBagType.COMMON)
    val UNCOMMON_LOOT_BAG: DeferredItem<Item> = registerLootBagItem(LootBagType.UNCOMMON)
    val RARE_LOOT_BAG: DeferredItem<Item> = registerLootBagItem(LootBagType.RARE)
    val EPIC_LOOT_BAG: DeferredItem<Item> = registerLootBagItem(LootBagType.EPIC)
    val LEGENDARY_LOOT_BAG: DeferredItem<Item> = registerLootBagItem(LootBagType.LEGENDARY)
    val PATIENT_LOOT_BAG: DeferredItem<Item> = registerLootBagItem(LootBagType.PATIENT)
    val ARTIFICIAL_LOOT_BAG: DeferredItem<Item> = registerLootBagItem(LootBagType.ARTIFICIAL)
    val BACON_LOOT_BAG: DeferredItem<Item> = registerLootBagItem(LootBagType.BACON)

    val LOOT_BAGS: List<DeferredItem<Item>> = listOf(
        COMMON_LOOT_BAG,
        UNCOMMON_LOOT_BAG,
        RARE_LOOT_BAG,
        EPIC_LOOT_BAG,
        LEGENDARY_LOOT_BAG,
        PATIENT_LOOT_BAG,
        ARTIFICIAL_LOOT_BAG,
        BACON_LOOT_BAG
    )

    private fun registerLootBagItem(type: LootBagType): DeferredItem<Item> =
        ITEMS.register(type.itemId) { ->
            LootBagItem(type)
        }

    fun register(eventBus: IEventBus) {
        ITEMS.register(eventBus)
    }
}