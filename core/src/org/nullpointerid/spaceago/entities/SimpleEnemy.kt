package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.SpaceShooter.gameAtlas
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.entities.collectables.Collectible
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.get

class SimpleEnemy : EntityBase(), Destroyable {

    companion object {
        val TEXTURE = gameAtlas[RegionNames.SIMPLE_ENEMY]!!
        const val TEXTURE_WIDTH = 0.55f
        const val TEXTURE_HEIGHT = 1f

        const val BOUNDS_X_OFFSET = 0.13f
        const val BOUNDS_Y_OFFSET = 0.03f

        const val BOUNDS_WIDTH = 0.27f
        const val BOUNDS_HEIGHT = 0.93f

        const val MIN_X = -0.12f
        const val MAX_X = GameConfig.WORLD_WIDTH - 0.42f

        const val MAX_SPEED = 0.07f

        const val SCORE_VALUE = 100
    }

    val bound = Rectangle(0f, 0f, BOUNDS_WIDTH, BOUNDS_HEIGHT)
    val containsDropable = Collectible.collectibleChance()

    override val bounds = GdxArray<Rectangle>().apply { add(bound) }

    override fun update(delta: Float) {
        y -= MAX_SPEED
    }

    override fun updateBounds() {
        bound.setPosition(x + BOUNDS_X_OFFSET, y + BOUNDS_Y_OFFSET)
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
}