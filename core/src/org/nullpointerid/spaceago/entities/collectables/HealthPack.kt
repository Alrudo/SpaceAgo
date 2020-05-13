package org.nullpointerid.spaceago.entities.collectables

import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player

class HealthPack(x: Float, y: Float) : EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT), Collectible {

    companion object {
        const val TEXTURE_WIDTH = 0.3f
        const val TEXTURE_HEIGHT = 0.3f
    }

    private val healAmount = 0.2f

    override var lived = 0f

    override fun action(player: Player): Boolean {
        if (player.lives < GameConfig.LIVES_START) {
            player.lives = (player.lives + healAmount).coerceAtMost(GameConfig.LIVES_START)
            return true
        }
        return false
    }
}