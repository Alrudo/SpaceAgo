package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture


class Player(
        x: Float,
        y: Float
) : Entity(x, y, width, height, texture) {
    companion object {
        const val width = 128
        const val height = 128
        val texture = Texture("images/jet1.png")
    }

    private var health = 1f
    private var score = 0
    private var shootTimer = 0f

    fun getHealth(): Float {
        return health
    }

    fun setHealth(health: Float) {
        this.health = health
    }

    fun subtractFromHealth(damage: Float) {
        health -= damage
    }

    fun addToHealth(healAmount: Float) {
        health += healAmount
    }

    fun getScore(): Int {
        return score
    }

    fun addToScore(score: Int) {
        this.score += score
    }

    fun getShootTimer(): Float {
        return shootTimer
    }

    fun setShootTimer(shootTimer: Float) {
        this.shootTimer = shootTimer
    }

    fun addToShootTimer(shootTimer: Float) {
        this.shootTimer += shootTimer
    }
}
