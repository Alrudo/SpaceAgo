package org.nullpointerid.spaceago.collectables

import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.utils.GdxArray

class UltimateWeapon : Collectible, EntityBase() {

    private val bound = Rectangle(0f, 0f, 0.35f, 0.35f)

    override val bounds = GdxArray<Rectangle>().apply { add(bound) }
    override var lived = 0f

    override fun updateBounds() {
        bound.setPosition(x, y)
    }

    override fun action(player: Player): Boolean {
        TODO("Not yet implemented")
    }
}