package org.nullpointerid.spaceago.entities.projectile

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.utils.gdx.get

class EnemyBullet(x: Float, y: Float) : EntityBase(x, y, WIDTH, HEIGHT) {
    constructor() : this(0f, 0f) {

    }

    companion object {
        val TEXTURE = SpaceShooter.GAME_ATLAS[RegionNames.PLASMA_ENEMY]!!
        const val WIDTH = 0.09f
        const val HEIGHT = 0.24f

        const val MAX_SPEED = 3.5f
        const val DAMAGE = 0.08f
    }

    var vector: Vector2 = Vector2(0f, 1f)

    init {
        coreBound.rotation = 180f
    }

    override fun update(delta: Float, world: World) {
        y += vector.y * MAX_SPEED * delta
        x += vector.x * MAX_SPEED * delta
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun onCollide(entity: EntityBase) {
        when (entity) {
            is Player -> {
                entity.damage(DAMAGE)
                this.toRemove = true
            }
        }
    }
}