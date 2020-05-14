package org.nullpointerid.spaceago.tools

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.nullpointerid.spaceago.assets.AssetPaths
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.gdx.toInternalFile

class MovingBackground {

    companion object {
        const val DEFAULT_SPEED = 80
        private const val ACCELERATION = 50
        private const val REACH_ACCELERATION = 200
    }

    var finalSpeed = DEFAULT_SPEED
    var fixedSpeed = true

    private val img = Texture(AssetPaths.MOVING_BG.toInternalFile())
    private var y1 = 0f
    private var y2 = img.height.toFloat()
    private var speed = 0 // Pixels per second
    private var imageScale = GameConfig.HUD_WIDTH / img.height.toFloat()

    fun updateRender(delta: Float, batch: SpriteBatch) {
        if (speed < finalSpeed) {
            speed += REACH_ACCELERATION * delta.toInt()
            if (speed > finalSpeed) {
                speed = finalSpeed
            }
        } else if (speed > finalSpeed) {
            speed -= REACH_ACCELERATION * delta.toInt()
            if (speed < finalSpeed) {
                speed = finalSpeed
            }
        }
        if (!fixedSpeed) {
            speed += ACCELERATION * delta.toInt()
        }
        y1 -= speed * delta
        y2 -= speed * delta
        if (y1 + img.height * imageScale <= 0) {
            y1 = y2 + img.height * imageScale
        }
        if (y2 + img.height * imageScale <= 0) {
            y2 = y1 + img.height * imageScale
        }

        //Render
        batch.draw(img, 0f, y1, GameConfig.HUD_WIDTH, img.height * imageScale)
        batch.draw(img, 0f, y2, GameConfig.HUD_WIDTH, img.height * imageScale)
    }

    fun resize(width: Int, height: Int) {
        imageScale = width / img.height.toFloat()
    }
}
