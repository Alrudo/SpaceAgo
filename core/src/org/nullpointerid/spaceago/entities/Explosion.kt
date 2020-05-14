package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter.explosionTexture
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.utils.XRectangle

class Explosion(x: Float, y: Float) : EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT) {
    constructor() : this(0f, 0f) {

    }

    companion object {
        private const val TILE_WIDTH = 32
        private const val TILE_HEIGHT = 32

        const val TEXTURE_WIDTH = 1f
        const val TEXTURE_HEIGHT = 0.8f
        val animation = Animation(0.2f, *TextureRegion.split(explosionTexture, TILE_WIDTH, TILE_HEIGHT)[0])
    }

    var stateTime = 0f
    override val innerBounds: MutableList<XRectangle> = mutableListOf()

    override fun update(delta: Float, world: World) {
        stateTime += delta
        if (animation.isAnimationFinished(stateTime)) toRemove = true
    }

    override fun texture(): TextureRegion {
        return animation.getKeyFrame(stateTime)
    }
}