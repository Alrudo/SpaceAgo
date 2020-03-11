package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion


class Explosion(
        x: Float,
        y: Float
) : Entity(x, y, width, height, texture) {
    companion object {
        private const val FRAME_LENGTH = 0.2f
        private const val OFFSET = 8
        private const val width: Int = 32
        private const val height: Int = 32
        private val texture: Texture = Texture("images/explosion.png")
        private var anim = Animation(FRAME_LENGTH, *TextureRegion.split(texture, width, height)[0])
    }

    private var stateTime: Float

    init {
        this.posX = x - OFFSET
        this.posY = y - OFFSET
        stateTime = 0f
    }

    override fun update(deltaTime: Float) {
        stateTime += deltaTime
        if (anim.isAnimationFinished(stateTime)) remove = true
    }

    override fun render(batch: SpriteBatch) {
        batch.draw(anim.getKeyFrame(stateTime), posX, posY, width.toFloat(), height.toFloat())
    }
}
