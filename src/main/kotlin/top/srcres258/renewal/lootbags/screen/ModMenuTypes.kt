package top.srcres258.renewal.lootbags.screen

import net.minecraft.core.registries.Registries
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.network.IContainerFactory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.screen.custom.BagOpenerMenu
import top.srcres258.renewal.lootbags.screen.custom.BagStorageMenu
import top.srcres258.renewal.lootbags.screen.custom.LootRecyclerMenu

object ModMenuTypes {
    val MENUS: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, LootBags.MOD_ID)

    val BAG_STORAGE: DeferredHolder<MenuType<*>, MenuType<BagStorageMenu>> =
        registerContainerMenu("bag_storage") { containerId, inv, data ->
            BagStorageMenu(containerId, inv, inv.player.level(), data)
        }
    val BAG_OPENER: DeferredHolder<MenuType<*>, MenuType<BagOpenerMenu>> =
        registerContainerMenu("bag_opener") { containerId, inv, data ->
            BagOpenerMenu(containerId, inv, inv.player.level(), data)
        }
    val LOOT_RECYCLER: DeferredHolder<MenuType<*>, MenuType<LootRecyclerMenu>> =
        registerContainerMenu("loot_recycler") { containerId, inv, data ->
            LootRecyclerMenu(containerId, inv, inv.player.level(), data)
        }

    private fun <T : AbstractContainerMenu> registerContainerMenu(
        name: String,
        factory: IContainerFactory<T>
    ) = MENUS.register(name) { -> IMenuTypeExtension.create(factory) }

    fun register(eventBus: IEventBus) {
        MENUS.register(eventBus)
    }
}