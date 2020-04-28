package org.nullpointerid.spaceago.collectables

import org.nullpointerid.spaceago.entities.Player

interface Collectible {

    companion object {

        const val MAX_LIFE_TIME = 4f
    }

    var lived: Float

    fun action(player: Player): Boolean
}
