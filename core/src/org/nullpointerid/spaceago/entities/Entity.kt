package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.nullpointerid.spaceago.screen.MainGameScreenOld


abstract class Entity(var posX: Float, var posY: Float, val width: Int, val height: Int, val texture: Texture) {
    var remove: Boolean = false

    open fun changePos(x: Float, y: Float) {
        this.posX = x
        this.posY = y
    }

    open fun collidesWith(react: Entity): Boolean {
        return posX < react.posX + react.width && posY < react.posY + react.height && posX + width > react.posX && posY + height > react.posY
    }

    open fun action(scene: MainGameScreenOld) {}

    open fun update(deltaTime: Float) {}

    open fun render(batch: SpriteBatch) {
        batch.draw(texture, posX, posY)
    }
}
