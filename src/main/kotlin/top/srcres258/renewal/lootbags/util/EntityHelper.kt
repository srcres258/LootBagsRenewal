package top.srcres258.renewal.lootbags.util

import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

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