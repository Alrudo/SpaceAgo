package org.nullpointerid.spaceago.collectables

import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.utils.GdxArray

class UltimateWeapon : Collectible, EntityBase() {

    companion object {

        const val TEXTURE_WIDTH = 0.35f
        const val TEXTURE_HEIGHT = 0.35f
    }

    private val bound = Rectangle(0f, 0f, TEXTURE_WIDTH, TEXTURE_HEIGHT)

    override val bounds = GdxArray<Rectangle>().apply { add(bound) }
    override var lived = 0f

    override fun updateBounds() {
        bound.setPosition(x, y)
    }

    override fun action(player: Player): Boolean {
        if (player.ultimateWeapon < Player.MAX_ULTIMATE_WEAPON_COUNT) {
            player.ultimateWeapon += 1
            return true
        }
        return false
    }
}