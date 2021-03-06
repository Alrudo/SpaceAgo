package org.nullpointerid.spaceago.entities.collectables

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.utils.gdx.get

class UltimateWeapon(x: Float, y: Float) : Collectible, EntityBase(x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT) {
    constructor() : this(0f, 0f)

    companion object {
        val TEXTURE = SpaceShooter.GAME_ATLAS[RegionNames.AMMO_CRATE]!!
        const val TEXTURE_WIDTH = 0.35f
        const val TEXTURE_HEIGHT = 0.35f
    }

    override var lived = 4f

    override fun update(delta: Float, world: World) {
        lived -= delta
        if (lived <= 0){
            toRemove= true
        }
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun canCollideWith(entity: EntityBase): Boolean {
        return entity is Player
    }

    override fun onCollide(entity: EntityBase) {
        if (canCollideWith(entity)) {
            action(entity as Player)
        }
    }

    override fun action(player: Player): Boolean {
        if (player.ultimateWeapon < Player.MAX_ULTIMATE_WEAPON_COUNT) {
            player.ultimateWeapon += 1
            toRemove = true
            return true
        }
        return false
    }
}