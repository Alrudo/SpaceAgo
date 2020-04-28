package org.nullpointerid.spaceago.collectables

import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.utils.GdxArray

class MoneyCrate : Collectible, EntityBase() {

    companion object {
        const val SCORE_VALUE = 500
    }

    private val bound = Rectangle(0f, 0f, 0f, 0f)

    override val bounds = GdxArray<Rectangle>()
    override var lived = 0f

    override fun updateBounds() {
    }

    override fun action(player: Player): Boolean {
        // TODO: Add SCORE_VALUE to player score after the Issue with moving score to Player class is done.
        return true
    }
}