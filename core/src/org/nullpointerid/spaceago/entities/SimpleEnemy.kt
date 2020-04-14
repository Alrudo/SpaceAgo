package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.GdxArray

class SimpleEnemy : EntityBase() {

    companion object {
        const val TEXTURE_WIDTH = 0.55f
        const val TEXTURE_HEIGHT = 1f

        const val BOUNDS_X_OFFSET = 0.13f
        const val BOUNDS_Y_OFFSET = 0.03f

        const val BOUNDS_WIDTH = 0.27f
        const val BOUNDS_HEIGHT = 0.93f

        const val MIN_X = -0.12f
        const val MAX_X = GameConfig.WORLD_WIDTH - 0.42f

        const val MAX_SPEED = 0.1f
    }

    val bound = Rectangle(0f, 0f, BOUNDS_WIDTH, BOUNDS_HEIGHT)
    override val bounds = GdxArray<Rectangle>().apply { add(bound) }

    fun update() {
        y -= MAX_SPEED
    }

    override fun updateBounds() {
        bound.setPosition(x + BOUNDS_X_OFFSET, y + BOUNDS_Y_OFFSET)
    }
}