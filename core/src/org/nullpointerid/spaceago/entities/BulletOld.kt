package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture
import org.nullpointerid.spaceago.SpaceShooterOld
import org.nullpointerid.spaceago.screen.MainGameScreenOld


class BulletOld(
        x: Float,
        y: Float
) : EntityOld(x, y, width, height, texture) {
    companion object {
        private const val width: Int = 3
        private const val height: Int = 12
        private val texture: Texture = Texture("images/bullet.png")
        const val SPEED = 500
    }

    override fun action(scene: MainGameScreenOld) {
        scene.entities.filterIsInstance<EnemyOld>().forEach { enemy ->
            if (this.collidesWith(enemy)) {
                this.remove = true
                enemy.remove = true
                scene.addEntity(ExplosionOld(enemy.posX + 24f, enemy.posY))
                scene.player.score += 100
            }
        }
    }

    override fun update(deltaTime: Float) {
        posY += SPEED * deltaTime
        if (posY > SpaceShooterOld.HEIGHT) remove = true
        changePos(posX, posY)
    }
}
