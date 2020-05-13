package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter.gameAtlas
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.get
import kotlin.random.Random

class SimpleEnemy(x: Float, y: Float) : EntityBase(x, y, WIDTH, HEIGHT), Destroyable {
    constructor() : this(0f, 0f) {

    }

    companion object {
        val TEXTURE = gameAtlas[RegionNames.SIMPLE_ENEMY]!!
        const val WIDTH = 0.255f
        const val HEIGHT = 0.96f

        const val MIN_X = 0.12f
        const val MAX_X = GameConfig.WORLD_WIDTH - 0.12f

        const val MAX_SPEED = 2f

        const val SCORE_VALUE = 100
    }

    var rotationSpeed = Random.nextDouble(13.0, 39.0).toFloat() * Random.nextDouble(-1.0, 1.0).toFloat()
    var xSpeed = (Random.nextFloat() - 0.5f) * 2

    override fun update(delta: Float) {
        y -= MAX_SPEED * delta
        x -= xSpeed * delta
        coreBound.rotation += rotationSpeed * delta
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

    override fun getMaxHealth(): Float {
        TODO("Not yet implemented")
    }

    override fun getHealth(): Float {
        TODO("Not yet implemented")
    }

    override fun setHealth(amount: Float) {
        TODO("Not yet implemented")
    }
}

