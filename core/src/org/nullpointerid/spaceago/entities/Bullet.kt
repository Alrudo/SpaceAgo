package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.get

class Bullet(val owner: Player) : EntityBase() {

    companion object {

        val TEXTURE = SpaceShooter.gameAtlas[RegionNames.BULLET]!!
        const val TEXTURE_WIDTH = 0.25f
        const val TEXTURE_HEIGHT = 0.25f

        const val BOUNDS_WIDTH = 0.1f
        const val BOUNDS_HEIGHT = 0.2f

        const val MAX_SPEED = 0.15f
    }

    private val bound = Rectangle(0f, 0f, BOUNDS_WIDTH, BOUNDS_HEIGHT)
    override val bounds = GdxArray<Rectangle>().apply { add(bound) }

    override fun update(delta: Float) {
        y += MAX_SPEED
    }

    override fun updateBounds() {
        bound.setPosition(x, y)
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun textureWidth(): Float {
        return TEXTURE_WIDTH
    }

    override fun textureHeight(): Float {
        return TEXTURE_HEIGHT
    }

    override fun onCollide(entity: EntityBase) {
        when (entity) {
            is CivilianShip -> {
                owner.score += CivilianShip.SCORE_VALUE
                entity.toRemove = true
                this.toRemove = true
            }
            is SimpleEnemy -> {
                owner.score += SimpleEnemy.SCORE_VALUE
                entity.toRemove = true
                this.toRemove = true
            }
        }
    }
}