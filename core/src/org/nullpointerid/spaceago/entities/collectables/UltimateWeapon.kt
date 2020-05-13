package org.nullpointerid.spaceago.entities.collectables

import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player

class UltimateWeapon(x: Float, y: Float) : Collectible, EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT) {

    companion object {
        const val TEXTURE_WIDTH = 0.35f
        const val TEXTURE_HEIGHT = 0.35f
    }

    override var lived = 0f

    override fun action(player: Player): Boolean {
        if (player.ultimateWeapon < Player.MAX_ULTIMATE_WEAPON_COUNT) {
            player.ultimateWeapon += 1
            return true
        }
        return false
    }
}