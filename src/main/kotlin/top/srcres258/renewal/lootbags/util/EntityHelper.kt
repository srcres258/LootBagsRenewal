package top.srcres258.renewal.lootbags.util

import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.*

fun Entity.setShootMovementFromRotation(
    shooter: Entity,
    xRot: Float,
    yRot: Float,
    zRot: Float,
    velocity: Float,
    inaccuracy: Float
) {
    setShootMovementFromRotation(
        shooter.knownMovement,
        xRot,
        yRot,
        zRot,
        velocity,
        inaccuracy,
        shooter.onGround()
    )
}

fun Entity.setShootMovementFromRotation(
    movement: Vec3,
    xRot: Float,
    yRot: Float,
    zRot: Float,
    velocity: Float,
    inaccuracy: Float,
    onGround: Boolean = true
) {
    val f = -sin(yRot * (PI / 180.0)) * cos(xRot * (PI / 180.0))
    val f1 = -sin((xRot + zRot) * (PI / 180.0))
    val f2 = cos(yRot * (PI / 180.0)) * cos(xRot * (PI / 180.0))
    setShootMovement(f, f1, f2, velocity, inaccuracy)
    deltaMovement = deltaMovement.add(movement.x, if (onGround) 0.0 else movement.y, movement.z)
}

fun Entity.setShootMovement(
    x: Double,
    y: Double,
    z: Double,
    velocity: Float,
    inaccuracy: Float
) {
    val vec3 = getMovementToShoot(x, y, z, velocity, inaccuracy)
    deltaMovement = vec3
    hasImpulse = true
    val d0 = vec3.horizontalDistance()
    yRot = atan2(vec3.x, vec3.z).toFloat() * 180F / PI.toFloat()
    xRot = (atan2(vec3.y, d0) * 180F / PI.toFloat()).toFloat()
    yRotO = yRot
    xRotO = xRot
}

private fun Entity.getMovementToShoot(
    x: Double,
    y: Double,
    z: Double,
    velocity: Float,
    inaccuracy: Float
) = Vec3(x, y, z)
    .normalize()
    .add(
        random.triangle(0.0, 0.0172275 * inaccuracy.toDouble()),
        random.triangle(0.0, 0.0172275 * inaccuracy.toDouble()),
        random.triangle(0.0, 0.0172275 * inaccuracy.toDouble())
    )
    .scale(velocity.toDouble())

fun newItemEntityForDropping(level: Level, location: Vec3, stack: ItemStack): ItemEntity {
    val entityLoc = calcItemEntityLocationForDropping(location, level.random)

    val entity = ItemEntity(level, entityLoc.x, entityLoc.y, entityLoc.z, stack)
    entity.setDeltaMovement(
        level.random.triangle(0.0, 0.11485000171139836),
        level.random.triangle(0.2, 0.11485000171139836),
        level.random.triangle(0.0, 0.11485000171139836)
    )
    return entity
}

fun newItemEntitiesForDropping(level: Level, location: Vec3, stack: ItemStack): List<ItemEntity> {
    val entityLoc = calcItemEntityLocationForDropping(location, level.random)

    val remainingStack = stack.copy()
    val result = mutableListOf<ItemEntity>()
    while (!remainingStack.isEmpty) {
        val entity = ItemEntity(level, entityLoc.x, entityLoc.y, entityLoc.z,
            remainingStack.split(level.random.nextInt(21) + 10))
        entity.setDeltaMovement(
            level.random.triangle(0.0, 0.11485000171139836),
            level.random.triangle(0.2, 0.11485000171139836),
            level.random.triangle(0.0, 0.11485000171139836)
        )
        result.add(entity)
    }
    return result
}

fun newItemEntitiesForDropping(level: Level, location: Vec3, stacks: List<ItemStack>): List<ItemEntity> =
    mutableListOf<ItemEntity>().also { result ->
        for (stack in stacks) {
            result.addAll(newItemEntitiesForDropping(level, location, stack))
        }
    }

private fun calcItemEntityLocationForDropping(location: Vec3, random: RandomSource): Vec3 {
    val d0 = EntityType.ITEM.width
    val d1 = 1.0 - d0
    val d2 = d0 / 2.0
    val x = floor(location.x) + random.nextDouble() * d1 + d2
    val y = floor(location.y) + random.nextDouble() * d1
    val z = floor(location.z) + random.nextDouble() * d1 + d2

    return Vec3(x, y, z)
}