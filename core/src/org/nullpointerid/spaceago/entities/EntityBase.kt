package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Intersector
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.utils.XRectangle
import org.nullpointerid.spaceago.utils.get
import java.io.Serializable


abstract class EntityBase(x: Float, y: Float, var width: Float = 1f, var height: Float = 1f) : Serializable {

    var x: Float = x
        set(value) {
            field = value
            updateBounds() // Make changes in entity x be represented on screen.
        }

    var y: Float = y
        set(value) {
            field = value
            updateBounds() // Make changes in entity y be represented on screen.
        }

    var toRemove = false

    open val coreBound = XRectangle(x - width / 2, y - height / 2, width, height)
    open val innerBounds: MutableList<XRectangle> = mutableListOf(XRectangle(x, y, width, height).bindToRect(coreBound))

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    open fun updateBounds() {
        coreBound.setPosition(x - width / 2, y - height / 2)
        innerBounds.forEach { it.setPosition(coreBound.x, coreBound.y) }
    }

    open fun canCollideWith(entity: EntityBase): Boolean {
        return false
    }

    open fun isCollidingWith(entity: EntityBase): Boolean {
        if (!Intersector.overlapConvexPolygons(coreBound, entity.coreBound)) {
            return false
        }
        for (bound in innerBounds) {
            for (enemyBound in entity.innerBounds) {
                if (Intersector.overlapConvexPolygons(bound, enemyBound)) return true
            }
        }
        return false
    }

    open fun onCollide(entity: EntityBase) {
    }

    open fun onDestroy() {
    }

    open fun update(delta: Float) {
    }

    open fun texture(): TextureRegion {
        return SpaceShooter.gameAtlas[RegionNames.LASER_BEAM]!!
    }

    open fun textureWidth(): Float {
        return 1f;
    }

    open fun textureHeight(): Float {
        return 1f;
    }

}