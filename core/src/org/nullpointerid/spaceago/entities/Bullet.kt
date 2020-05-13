package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.utils.get

class Bullet(x: Float, y: Float, var owner: Player? = null) : EntityBase(x, y, WIDTH, HEIGHT) {
    constructor() : this(0f, 0f) {

    }

    companion object {
        val TEXTURE = SpaceShooter.gameAtlas[RegionNames.BULLET]!!
        const val WIDTH = 0.09f
        const val HEIGHT = 0.24f

        const val MAX_SPEED = 4.5f
        const val DAMAGE = 0.1f
    }

    override fun update(delta: Float) {
        y += MAX_SPEED * delta
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun textureWidth(): Float {
        return WIDTH
    }

    override fun textureHeight(): Float {
        return HEIGHT
    }

    override fun onCollide(entity: EntityBase) {
        when (entity) {
            is CivilianShip -> {
                owner!!.score += CivilianShip.SCORE_VALUE
                entity.damage(DAMAGE)
                this.toRemove = true
            }
            is SimpleEnemy -> {
                owner!!.score += SimpleEnemy.SCORE_VALUE
                entity.toRemove = true
                this.toRemove = true
            }
        }
    }
}