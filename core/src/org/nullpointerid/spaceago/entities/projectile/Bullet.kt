package org.nullpointerid.spaceago.entities.projectile

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.utils.gdx.get

class Bullet(x: Float, y: Float, var owner: Player? = null) : EntityBase(x, y, WIDTH, HEIGHT) {
    constructor() : this(0f, 0f) {

    }

    companion object {
        val TEXTURE = SpaceShooter.GAME_ATLAS[RegionNames.PLASMA]!!
        const val WIDTH = 0.12f
        const val HEIGHT = 0.32f

        const val MAX_SPEED = 7.5f
        const val DAMAGE = 0.1f
    }

    var vector: Vector2 = Vector2(0f, 1f)
    set(value) {
        field = value
        coreBound.rotation = vector.angle()-90
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
            is Destroyable -> {
                entity.damage(DAMAGE)
                if(entity.isDead()){
                    owner!!.score += entity.getScore()
                    entity.toRemove = true
                }
                this.toRemove = true
            }
//            is SimpleEnemy -> {
//                owner!!.score += entity.getScore()
//                entity.toRemove = true
//                this.toRemove = true
//            }
        }
    }
}