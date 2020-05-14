package org.nullpointerid.spaceago.entities.collectables

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter.GAME_ATLAS
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.utils.gdx.get

class HealthPack(x: Float, y: Float) : EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT), Collectible {
    constructor() : this(0f, 0f)

    companion object {
        val TEXTURE = GAME_ATLAS[RegionNames.HEALTH_PACK]!!
        const val TEXTURE_WIDTH = 0.3f
        const val TEXTURE_HEIGHT = 0.3f
    }

    private val healAmount = 0.2f
    override var lived = 4f

    override fun update(delta: Float, world: World) {
        lived -= delta
        if (lived <= 0){
            toRemove= true
        }
    }

    override fun canCollideWith(entity: EntityBase): Boolean {
        return entity is Player
    }

    override fun onCollide(entity: EntityBase) {
        if(canCollideWith(entity)){
            action(entity as Player)
        }
    }

    override fun action(player: Player): Boolean {
        if (player.lives < player.maxHealth()) {
            player.lives = (player.lives + healAmount).coerceAtMost(player.maxHealth())
            toRemove = true
            return true
        }
        return false
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }
}