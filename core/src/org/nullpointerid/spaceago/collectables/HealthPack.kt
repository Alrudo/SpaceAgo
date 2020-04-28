package org.nullpointerid.spaceago.collectables

import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.utils.GdxArray

class HealthPack : Collectible, EntityBase() {

    private val bound = Rectangle(0f, 0f, 0.3f, 0.3f)
    private val healAmount = 0.2f

    override var lived = 0f
    override val bounds = GdxArray<Rectangle>().apply { add(bound) }

    override fun updateBounds() {
        bound.setPosition(x, y)
    }

    override fun action(player: Player): Boolean {
        if (player.lives < GameConfig.LIVES_START) {
            player.lives = Math.min(GameConfig.LIVES_START, player.lives + healAmount)
            return true
        }
        return false
    }
}