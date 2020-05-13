package org.nullpointerid.spaceago.entities.collectables

import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player

class MoneyCrate(x: Float, y: Float) : Collectible, EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT) {

    companion object {
        const val SCORE_VALUE = 500

        const val TEXTURE_WIDTH = 0.35f
        const val TEXTURE_HEIGHT = 0.35f
    }

    override var lived = 0f

    override fun action(player: Player): Boolean {
        player.score += SCORE_VALUE
        return true
    }
}