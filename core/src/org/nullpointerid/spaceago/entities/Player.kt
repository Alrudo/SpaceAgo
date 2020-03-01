package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture

class Player(
        x: Float,
        y: Float
) : Entity(x, y, width, height, texture) {
    companion object {
        const val width: Int = 128
        const val height: Int = 128
        val texture = Texture("images/jet1.png")
    }

    var health = 1f
    var score = 0
    var shootTimer = 0f

}