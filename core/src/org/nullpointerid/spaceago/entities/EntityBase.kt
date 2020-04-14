package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.utils.GdxArray

abstract class EntityBase {

    var x: Float = 0f
        set(value) {
            field = value
            updateBounds() // Make changes in entity x be represented on screen.
        }

    var y: Float = 0f
        set(value) {
            field = value
            updateBounds() // Make changes in entity y be represented on screen.
        }

    abstract val bounds: GdxArray<Rectangle>

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    abstract fun updateBounds()

    open fun isCollidingWith(gameObject: EntityBase): Boolean {
        for (bound in bounds) {
            for (enemyBound in gameObject.bounds) {
                if (Intersector.overlaps(bound, enemyBound)) return true
            }
        }
        return false
    }
}