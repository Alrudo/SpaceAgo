package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.utils.GdxArray

class CivilianShip(val toLeft: Boolean) : EntityBase() {

    companion object {
        const val TEXTURE_WIDTH = 2f
        const val TEXTURE_HEIGH = 1f

        const val BODY_HEIGHT = 1f
        const val NOSE_HEIGHT = 0.6f

        const val BODY_WIDTH = 1f
        const val NOSE_WIDTH = 1f

        const val MAX_SPEED = 0.02f
    }

    var lives = 0.2f

    private val noseBound = Rectangle(0f, 0f, NOSE_WIDTH, NOSE_HEIGHT)
    private val bodyBound = Rectangle(0f, 0f, BODY_WIDTH, BODY_HEIGHT)

    override val bounds = GdxArray<Rectangle>().apply { add(bodyBound, noseBound) }

    fun update() {
        if (toLeft) x -= MAX_SPEED
        else x += MAX_SPEED
    }

    override fun updateBounds() {
        if (toLeft) {
            noseBound.setPosition(x, y + 0.2f)
            bodyBound.setPosition(x + noseBound.width, y)
        } else {
            bodyBound.setPosition(x, y)
            noseBound.setPosition(x + bodyBound.width, y + 0.2f)
        }
    }
}