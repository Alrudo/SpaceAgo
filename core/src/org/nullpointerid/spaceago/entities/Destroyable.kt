package org.nullpointerid.spaceago.entities

import kotlin.math.max

interface Destroyable {

    fun getMaxHealth(): Float
    fun getHealth(): Float
    fun setHealth(amount: Float)
    fun damage(dam: Float) {
        setHealth(max(0f, getHealth() - dam))
    }

    fun isDead(): Boolean {
        return getHealth() == 0f
    }
}