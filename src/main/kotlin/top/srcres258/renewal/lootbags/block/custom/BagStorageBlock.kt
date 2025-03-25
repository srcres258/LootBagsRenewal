package top.srcres258.renewal.lootbags.block.custom

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import top.srcres258.renewal.lootbags.block.entity.ModBlockEntities
import top.srcres258.renewal.lootbags.block.entity.custom.BagStorageBlockEntity

class BagStorageBlock(
    properties: Properties = Properties.of()
        .strength(3.5F)
        .requiresCorrectToolForDrops()
) : BaseEntityBlock(properties) {
    companion object {
        private val CODEC: MapCodec<BagStorageBlock> = simpleCodec(::BagStorageBlock)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = BagStorageBlockEntity(pos, state)

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? = if (level.isClientSide) {
        null
    } else {
        createTickerHelper(blockEntityType, ModBlockEntities.BAG_STORAGE.get()) { level1, pos, state1, blockEntity ->
            blockEntity.tick(level1, pos, state1)
        }
    }

    // NOTE to override this method and return this value,
    // otherwise this block will **NOT** be rendered by the game and consequently
    // become invisible!!!
    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS
        } else {
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity != null && blockEntity is BagStorageBlockEntity) {
                player.openMenu(blockEntity, pos)
                return ItemInteractionResult.CONSUME
            } else {
                throw IllegalStateException("The BlockEntity at $pos is not a BagStorageBlockEntity")
            }
        }
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        // From NeoForge's official documentation:
        //
        // To make sure that caches can correctly update their stored capability, modders must call
        // level.invalidateCapabilities(pos) whenever a capability changes, appears, or disappears.
        level.invalidateCapabilities(pos)

        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    override fun playerWillDestroy(level: Level, pos: BlockPos, state: BlockState, player: Player): BlockState {
        // Make the block to be dropped into the world as well in creative mode. Vanilla will automatically
        // do dropping behavior for survival mode.
        val blockEntity = level.getBlockEntity(pos)
        if (blockEntity != null && blockEntity is BagStorageBlockEntity) {
            if (!level.isClientSide && player.isCreative && blockEntity.storedBagAmount > 0) {
                val stack = ItemStack(this)
                stack.applyComponents(blockEntity.collectComponents())
                val entity = ItemEntity(level, pos.x.toDouble() + 0.5, pos.y.toDouble() + 0.5, pos.z.toDouble() + 0.5, stack)
                entity.setDefaultPickUpDelay()
                level.addFreshEntity(entity)
            }
        }

        return super.playerWillDestroy(level, pos, state, player)
    }
}