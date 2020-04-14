package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture


class PlayerOld(
        x: Float,
        y: Float
) : EntityOld(x, y, width, height, texture) {
    companion object {
        const val width = 128
        const val height = 128
        val texture = Texture("images/jet1.png")
    }

    var health = 1f
        set(value) {
            field = when {
                health - value >= 0f -> health - value
                else -> 0f
            }
        }
    var score = 0
    var shootTimer = 0f
}
