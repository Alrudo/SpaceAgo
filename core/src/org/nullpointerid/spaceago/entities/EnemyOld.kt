package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.nullpointerid.spaceago.SpaceShooterOld
import org.nullpointerid.spaceago.screen.MainGameScreenOld


class EnemyOld(
        x: Float,
        y: Float = SpaceShooterOld.HEIGHT.toFloat()
) : EntityOld(x, y, width, height, texture) {
    companion object {
        const val width: Int = 64
        const val height: Int = 64
        private val texture: Texture = Texture("images/corona1.png")
        const val SPEED = 250
    }

    override fun action(scene: MainGameScreenOld) {
        if (collidesWith(scene.player)) {
            this.remove = true
            scene.player.health = 0.1f
        }
    }

    override fun update(deltaTime: Float) {
        posY -= SPEED * deltaTime
        if (posY < -height) remove = true
        changePos(posX, posY)
    }

    override fun render(batch: SpriteBatch) {
        batch.draw(texture, posX, posY, width.toFloat(), height.toFloat())
    }
}