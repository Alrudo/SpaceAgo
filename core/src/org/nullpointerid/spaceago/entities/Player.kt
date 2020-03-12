package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture
import kotlin.math.max


class Player(
        x: Float,
        y: Float
) : Entity(x, y, width, height, texture) {
    companion object {
        const val width = 128
        const val height = 128
        val texture = Texture("images/jet1.png")

    }

    var shootTimer = 0f
    var health = 1f
        private set
    var score = 0
        private set

    fun damage(damage: Float) {
        health -= max(0f, damage)
    }

    fun heal(heal: Float) {
        health += max(0f, heal)
    }

    fun addScore(score: Int) {
        this.score += max(0, score)
    }

    fun addToShootTimer(shootTimer: Float) {
        this.shootTimer += shootTimer
    }
}
