package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.utils.XRectangle
import org.nullpointerid.spaceago.utils.gdx.get

class CivilianShip(x: Float, y: Float, val toLeft: Boolean = false) : EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT), Destroyable {
    constructor() : this(0f, 0f) {

    }

    companion object {
        val TEXTURE1 = SpaceShooter.GAME_ATLAS[RegionNames.CIVILIAN_SHIP_LEFT]!!
        val TEXTURE2 = SpaceShooter.GAME_ATLAS[RegionNames.CIVILIAN_SHIP_RIGHT]!!
        const val TEXTURE_WIDTH = 3.2f
        const val TEXTURE_HEIGHT = 0.8f

        const val BODY_HEIGHT = TEXTURE_HEIGHT
        const val NOSE_HEIGHT = 0.6f * TEXTURE_HEIGHT

        const val BODY_WIDTH = 0.5f * TEXTURE_WIDTH
        const val NOSE_WIDTH = 0.5f * TEXTURE_WIDTH

        const val MAX_HEALTH = 2.5f
        const val MAX_SPEED = 1.5f

        const val SCORE_VALUE = -500
    }

    private var health = getMaxHealth()

    private val noseBound = XRectangle(0f, 0f, NOSE_WIDTH, NOSE_HEIGHT).bindToRect(coreBound,
            if (!toLeft) width / 2 else 0f,
            0.2f * TEXTURE_HEIGHT
    )
    private val bodyBound = XRectangle(0f, 0f, BODY_WIDTH, BODY_HEIGHT).bindToRect(coreBound,
            if (toLeft) width / 2 else 0f
    )

    override val innerBounds = mutableListOf(bodyBound, noseBound)

    init {
        updateBounds()
    }

    override fun update(delta: Float, world: World) {
        if (toLeft) x -= MAX_SPEED * delta
        else x += MAX_SPEED * delta
    }

    override fun updateBounds() {
        coreBound.setPosition(x - TEXTURE_WIDTH / 2, y - TEXTURE_HEIGHT / 2)
        if (toLeft) {
            noseBound.setPosition(coreBound.x, coreBound.y + 0.12f)
            bodyBound.setPosition(coreBound.x + NOSE_WIDTH, coreBound.y)
        } else {
            bodyBound.setPosition(coreBound.x, coreBound.y)
            noseBound.setPosition(coreBound.x + BODY_WIDTH, coreBound.y + 0.12f)
        }
    }

    override fun texture(): TextureRegion {
        return if (toLeft) TEXTURE1 else TEXTURE2
    }

    override fun getMaxHealth(): Float {
        return MAX_HEALTH
    }

    override fun getHealth(): Float {
        return health
    }

    override fun setHealth(amount: Float) {
        health = amount
        if(isDead()){
            toRemove = true
        }
    }

    override fun getScore(): Int {
        return SCORE_VALUE
    }

}