package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.get

class CivilianShip(val toLeft: Boolean) : EntityBase(), Destroyable {

    companion object {
        val TEXTURE1 = SpaceShooter.gameAtlas[RegionNames.CIVILIAN_SHIP_LEFT]!!
        val TEXTURE2 = SpaceShooter.gameAtlas[RegionNames.CIVILIAN_SHIP_RIGHT]!!
        const val TEXTURE_WIDTH = 2f
        const val TEXTURE_HEIGHT = 1f

        const val BODY_HEIGHT = 1f
        const val NOSE_HEIGHT = 0.6f

        const val BODY_WIDTH = 1f
        const val NOSE_WIDTH = 1f

        const val MAX_SPEED = 0.02f

        const val SCORE_VALUE = -500
    }

    var lives = 0.2f

    private val noseBound = Rectangle(0f, 0f, NOSE_WIDTH, NOSE_HEIGHT)
    private val bodyBound = Rectangle(0f, 0f, BODY_WIDTH, BODY_HEIGHT)

    override val bounds = GdxArray<Rectangle>().apply { add(bodyBound, noseBound) }

    override fun update(delta: Float) {
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

    override fun texture(): TextureRegion {
        return if (toLeft) TEXTURE1 else TEXTURE2
    }

    override fun textureWidth(): Float {
        return TEXTURE_WIDTH
    }

    override fun textureHeight(): Float {
        return TEXTURE_HEIGHT
    }
}