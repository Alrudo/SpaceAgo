package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.get

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

    var toRemove = false

    abstract val bounds: GdxArray<Rectangle>

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    abstract fun updateBounds()

    open fun canCollideWith(entity: EntityBase): Boolean {
        return false
    }

    open fun isCollidingWith(entity: EntityBase): Boolean {
        for (bound in bounds) {
            for (enemyBound in entity.bounds) {
                if (Intersector.overlaps(bound, enemyBound)) return true
            }
        }
        return false
    }

    open fun onCollide(entity: EntityBase){

    }

    open fun onDestroy(){

    }

    open fun update(delta: Float) {
        updateBounds()
    }

    open fun texture(): TextureRegion{
        return SpaceShooter.gameAtlas[RegionNames.LASER_BEAM]!!
    }

    open fun textureWidth(): Float{
        return 1f;
    }

    open fun textureHeight(): Float{
        return 1f;
    }

}