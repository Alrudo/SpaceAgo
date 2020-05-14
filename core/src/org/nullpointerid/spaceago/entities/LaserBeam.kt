package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter.GAME_ATLAS
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.gdx.get

class LaserBeam(x: Float, y: Float, val owner: Player) : EntityBase(x, y) {

    companion object {

        val TEXTURE = GAME_ATLAS[RegionNames.LASER_BEAM]!!
        const val TEXTURE_WIDTH = 0.25f
        const val TEXTURE_HEIGHT = GameConfig.WORLD_HEIGHT

        const val LIVE_TIME = 3f
    }

    var lived = 0f
    var using = false

    override fun onCollide(entity: EntityBase) {
        entity.toRemove = true
        when (entity) {
            is CivilianShip -> owner.score += entity.getScore()
            is SimpleEnemy -> owner.score += entity.getScore()
            is ShootingEnemy -> owner.score += entity.getScore()
        }
    }

    override fun update(delta: Float, world: World) {
        lived += delta
        if (lived >= LIVE_TIME) toRemove = true
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }
}