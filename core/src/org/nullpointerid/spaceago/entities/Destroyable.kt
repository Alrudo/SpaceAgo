package org.nullpointerid.spaceago.entities

import kotlin.math.max

interface Destroyable {

    fun getMaxHealth(): Float
    fun getHealth(): Float
    fun setHealth(amount: Float)
    fun getScore(): Int = 0
    fun damage(dam: Float) {
        setHealth(max(0f, getHealth() - dam))
    }

    fun getCollideDamage(): Float = 0.2f

    fun isDead(): Boolean {
        return getHealth() == 0f
    }
}