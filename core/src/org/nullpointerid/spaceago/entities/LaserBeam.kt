package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.GdxArray

class LaserBeam(val owner: Player) : EntityBase() {

    companion object {

        const val TEXTURE_WIDTH = 0.25f
        const val TEXTURE_HEIGHT = GameConfig.WORLD_HEIGHT

        const val LIVE_TIME = 3f
    }
    var lived = 0f
    var used = false

    private val bound = Rectangle(0f, 0f, TEXTURE_WIDTH, GameConfig.WORLD_HEIGHT)
    override val bounds = GdxArray<Rectangle>().apply { add(bound) }

    override fun updateBounds() {
        if (!used) {
            bound.setPosition(-1f, -1f)
        } else {
            bound.setPosition(owner.bounds[0].x - owner.bounds[0].width / 2f + 0.01f,
                    owner.bounds[0].y + owner.bounds[0].height + 0.05f)
        }
    }
}