package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.screens.MainGameScreen


class Bullet(
        x: Float,
        y: Float
) : Entity(x, y, width, height, texture) {
    companion object {
        private const val width: Int = 3
        private const val height: Int = 12
        private val texture: Texture = Texture("images/bullet.png")
        const val SPEED = 500
    }

    override fun action(scene: MainGameScreen) {
        scene.entities.filterIsInstance<Enemy>().forEach { enemy ->
            if (this.collidesWith(enemy)) {
                this.remove = true
                enemy.remove = true
                scene.addEntity(Explosion(enemy.posX + 24f, enemy.posY))
                scene.player.addToScore(100)
            }
        }
    }

    override fun update(deltaTime: Float) {
        posY += SPEED * deltaTime
        if (posY > SpaceShooter.HEIGHT) remove = true
        changePos(posX, posY)
    }
}
