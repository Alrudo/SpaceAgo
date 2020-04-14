package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.utils.GdxArray

class Bullet : EntityBase() {

    companion object {

        const val TEXTURE_WIDTH = 0.25f
        const val TEXTURE_HEIGHT = 0.25f

        const val BOUNDS_X_OFFSET = 0.07f
        const val BOUNDS_Y_OFFSET = 0.04f

        const val BOUNDS_WIDTH = 0.1f
        const val BOUNDS_HEIGHT = 0.2f

        const val OFFSET_X_PLAYER = (Player.BOUNDS_HOR_X_OFFSET + Player.BOUNDS_HOR_WIDTH) / 2f - 0.038f
        const val OFFSET_Y_PLAYER = Player.BOUNDS_VER_Y_OFFSET + Player.BOUNDS_VER_HEIGHT

        const val MAX_SPEED = 0.15f
    }

    private val bound = Rectangle(0f, 0f, BOUNDS_WIDTH, BOUNDS_HEIGHT)
    override val bounds = GdxArray<Rectangle>().apply { add(bound) }

    fun update() {
        y += MAX_SPEED
    }

    override fun updateBounds() {
        bound.setPosition(x + BOUNDS_X_OFFSET, y + BOUNDS_Y_OFFSET)
    }
}