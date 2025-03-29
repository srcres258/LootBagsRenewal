package top.srcres258.renewal.lootbags.block.entity

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.block.entity.custom.BagOpenerBlockEntity
import top.srcres258.renewal.lootbags.block.entity.custom.BagStorageBlockEntity
import top.srcres258.renewal.lootbags.block.entity.custom.LootRecyclerBlockEntity
import java.util.function.Supplier

object ModBlockEntities {
    val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LootBags.MOD_ID)

    val BAG_STORAGE: Supplier<BlockEntityType<BagStorageBlockEntity>> =
        BLOCK_ENTITIES.register("bag_storage") { ->
            BlockEntityType(::BagStorageBlockEntity, ModBlocks.BAG_STORAGE.get())
        }
    val BAG_OPENER: Supplier<BlockEntityType<BagOpenerBlockEntity>> =
        BLOCK_ENTITIES.register("bag_opener") { ->
            BlockEntityType(::BagOpenerBlockEntity, ModBlocks.BAG_OPENER.get())
        }
    val LOOT_RECYCLER: Supplier<BlockEntityType<LootRecyclerBlockEntity>> =
        BLOCK_ENTITIES.register("loot_recycler") { ->
            BlockEntityType(::LootRecyclerBlockEntity, ModBlocks.LOOT_RECYCLER.get())
        }

    fun register(eventBus: IEventBus) {
        BLOCK_ENTITIES.register(eventBus)
    }
}