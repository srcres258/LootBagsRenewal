package top.srcres258.renewal.lootbags.event

import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.BlockDropsEvent
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.block.ModBlocks
import top.srcres258.renewal.lootbags.block.entity.custom.BagStorageBlockEntity
import top.srcres258.renewal.lootbags.component.ModDataComponents

@EventBusSubscriber(modid = LootBags.MOD_ID)
object ModEvents {
    @SubscribeEvent
    fun onBlockDrops(event: BlockDropsEvent) {
//        val blockEntity = event.blockEntity
//        if (blockEntity != null && blockEntity is BagStorageBlockEntity) {
//            // For BagStorageBlock's destroy, the stored bags need to be saved into the item stack
//            // which is going to be dropped.
//
//            val storedBags = blockEntity.storedBagAmount
//            val stack = ItemStack(ModBlocks.BAG_STORAGE.get())
//            stack.set(ModDataComponents.BAGS_STORED.get(), storedBags)
//
//            val pos = if (event.drops.isEmpty()) {
//                Vec3(event.pos.x.toDouble(), event.pos.y.toDouble(), event.pos.z.toDouble())
//            } else {
//                event.drops.first().position()
//            }
//            val itemEntity = ItemEntity(event.level, pos.x, pos.y, pos.z, stack)
//            if (event.drops.isNotEmpty()) {
//                event.drops.clear()
//            }
//            event.drops.add(itemEntity)
//        }
    }
}