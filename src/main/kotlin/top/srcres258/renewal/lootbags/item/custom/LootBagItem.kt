package top.srcres258.renewal.lootbags.item.custom

import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import top.srcres258.renewal.lootbags.util.LootBagType
import top.srcres258.renewal.lootbags.util.setShootMovementFromRotation

class LootBagItem(
    val type: LootBagType,
    properties: Properties = Properties().stacksTo(16)
) : Item(properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(usedHand)
        if (!level.isClientSide && level is ServerLevel) {
            val loots = type.lootGenerator.generateLoot(
                level,
                LootParams.Builder(level)
                    .withParameter(LootContextParams.THIS_ENTITY, player)
                    .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                    .withParameter(LootContextParams.ATTACKING_ENTITY, player)
                    .withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, player)
                    .withParameter(LootContextParams.ORIGIN, player.getPosition(0F))
                    .withParameter(LootContextParams.TOOL, stack)
            )
            for (loot in loots) {
                val pos = player.eyePosition
                val entity = ItemEntity(level, pos.x, pos.y - 0.1, pos.z, loot)
                entity.setShootMovementFromRotation(player, player.xRot, player.yRot, 0.0F, 0.25F, 0.1F)
                entity.setDefaultPickUpDelay()
                level.addFreshEntity(entity)
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this))
        stack.consume(1, player)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }
}