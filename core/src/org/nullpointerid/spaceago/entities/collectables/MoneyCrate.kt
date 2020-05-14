package org.nullpointerid.spaceago.entities.collectables

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.utils.gdx.get

class MoneyCrate(x: Float, y: Float) : Collectible, EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT) {

    companion object {
        val TEXTURE = SpaceShooter.GAME_ATLAS[RegionNames.TREASURE_CHEST]!!
        const val SCORE_VALUE = 500

        const val TEXTURE_WIDTH = 0.35f
        const val TEXTURE_HEIGHT = 0.35f
    }

    override var lived = 4f

    override fun update(delta: Float, world: World) {
        lived -= delta
        if (lived <= 0){
            toRemove= true
        }
    }//

    override fun canCollideWith(entity: EntityBase): Boolean {
        return entity is Player
    }

    override fun onCollide(entity: EntityBase) {
        if (canCollideWith(entity)) {
            action(entity as Player)
        }
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun action(player: Player): Boolean {
        player.score += SCORE_VALUE
        toRemove = true
        return true
    }
}