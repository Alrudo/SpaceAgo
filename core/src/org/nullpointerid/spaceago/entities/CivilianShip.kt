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

    private val NoseBound = Rectangle(0f, 0f, NOSE_WIDTH, NOSE_HEIGHT)
    private val BodyBound = Rectangle(0f, 0f, BODY_WIDTH, BODY_HEIGHT)

    override val bounds = GdxArray<Rectangle>().apply { add(BodyBound, NoseBound) }

    fun update() {
        if (toLeft) x -= MAX_SPEED
        else x += MAX_SPEED
    }

    override fun updateBounds() {
        if (toLeft) {
            NoseBound.setPosition(x, y + 0.2f)
            BodyBound.setPosition(x + NoseBound.width, y)
        } else {
            BodyBound.setPosition(x, y)
            NoseBound.setPosition(x + BodyBound.width, y + 0.2f)
        }
    }
}