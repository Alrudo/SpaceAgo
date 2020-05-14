package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter.GAME_ATLAS
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.projectile.Bullet
import org.nullpointerid.spaceago.utils.gdx.get

class LaserBeam(x: Float, y: Float, val owner: Player? = null) : EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT) {
    constructor() : this(0f, 0f)

    companion object {

        val TEXTURE = GAME_ATLAS[RegionNames.LASER_BEAM]!!
        const val TEXTURE_WIDTH = 0.25f
        const val TEXTURE_HEIGHT = GameConfig.WORLD_HEIGHT

        const val LIVE_TIME = 4f
    }

    var lived = LIVE_TIME

    override fun onCollide(entity: EntityBase) {
        when (entity) {
            is Destroyable -> {
                entity.damage(999f)
                if(entity.isDead()){
                    owner!!.score += entity.getScore()
                    entity.toRemove = true
                }
            }
        }
    }



    override fun update(delta: Float, world: World) {
        lived -= delta
        if(owner != null){
            x = owner.x
            y = owner.y + height/2 + owner.height/2
        }
        if (lived <= 0) toRemove = true
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }
}