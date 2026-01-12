package top.srcres258.renewal.lootbags.event

import net.minecraft.util.Mth
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent
import top.srcres258.renewal.lootbags.LootBags
import top.srcres258.renewal.lootbags.util.LootBagType
import top.srcres258.renewal.lootbags.util.newItemEntitiesForDropping

@EventBusSubscriber(modid = LootBags.MOD_ID)
object ModEvents {
    @SubscribeEvent
    fun onLivingDrops(event: LivingDropsEvent) {

        // Skip player entities to prevent loot bags from dropping when players are killed
        if (event.entity.type == EntityType.PLAYER) {
            return
        }
        // Try to add loot bags to the drops list whenever a living entity drops items.
        for (bagType in LootBagType.entries) {
            if (!bagType.droppable) {
                continue
            }

            val random = event.entity.level().random
            val rand = Mth.nextDouble(random, 0.0, 1.0)
            if (rand <= bagType.dropChance) {
                val entityPos = event.entity.getPosition(0F)
                val amount = random.nextIntBetweenInclusive(bagType.dropAmountRange.first.toInt(),
                    bagType.dropAmountRange.last.toInt())
                if (amount <= 0) {
                    continue
                }
                val stack = ItemStack(bagType.asItem(), amount)
                val entities = newItemEntitiesForDropping(event.entity.level(), entityPos, stack)
                event.drops.addAll(entities)

                LootBags.LOGGER.debug(
                    "Generated loot bags of type {} amount {} for LivingEntity {} at position {}, level {}",
                    bagType,
                    amount,
                    event.entity.type.description.string,
                    entityPos,
                    event.entity.level().dimension().location()
                )
                break
            }
        }
    }
}