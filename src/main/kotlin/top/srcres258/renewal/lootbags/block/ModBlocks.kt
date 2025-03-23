package top.srcres258.renewal.lootbags.block

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.block.custom.BagOpenerBlock
import top.srcres258.renewal.lootbags.block.custom.BagStorageBlock
import top.srcres258.renewal.lootbags.block.custom.LootRecyclerBlock
import top.srcres258.renewal.lootbags.item.ModItems

object ModBlocks {
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(LootBags.MOD_ID)

    val LOOT_RECYCLER: DeferredBlock<Block> = registerBlockWithItem("loot_recycler") {
        LootRecyclerBlock()
    }
    val BAG_OPENER: DeferredBlock<Block> = registerBlockWithItem("bag_opener") {
        BagOpenerBlock()
    }
    val BAG_STORAGE: DeferredBlock<Block> = registerBlockWithItem("bag_storage") {
        BagStorageBlock()
    }

    private fun <T : Block> registerBlockWithItem(name: String, block: () -> T) =
        BLOCKS.register(name, block).also { defBlock ->
            registerBlockItem(name, defBlock)
        }

    private fun <T : Block> registerBlockItem(name: String, block: DeferredBlock<T>) {
        ModItems.ITEMS.register(name) { ->
            BlockItem(block.get(), Item.Properties())
        }
    }

    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
    }
}
