package net.starly.core.jb.util

import net.starly.core.jb.version.nms.wrapper.WorldWrapper
import org.bukkit.Location

class FeatherLocation (
    val world: WorldWrapper,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float = .0f,
    val pitch: Float = .0f
) {

   fun toLocation() = Location(world.bukkitWorld, x, y, z, yaw, pitch)

}