package top.srcres258.renewal.lootbags.util

import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity

fun BlockEntity.setChangedAndUpdateBlock(level: Level?) {
    setChanged()
    level?.let { level ->
        if (!level.isClientSide) {
            level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL)
        }
    }
}