package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.SpaceShooter.gameAtlas
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.collectables.Collectible
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.get
import org.nullpointerid.spaceago.views.game.GameController
import kotlin.math.round

class Player : EntityBase() {

    companion object {
        val TEXTURE = gameAtlas[RegionNames.PLAYER]!!
        const val TEXTURE_WIDTH = 1f
        const val TEXTURE_HEIGHT = 1f

        const val BOUNDS_VER_X_OFFSET = 0.43f
        const val BOUNDS_HOR_X_OFFSET = 0.17f

        const val BOUNDS_VER_Y_OFFSET = 0.1f
        const val BOUNDS_HOR_Y_OFFSET = 0.3f

        const val BOUNDS_VER_WIDTH = 0.13f
        const val BOUNDS_HOR_WIDTH = 0.66f

        const val BOUNDS_VER_HEIGHT = 0.8f
        const val BOUNDS_HOR_HEIGHT = 0.25f

        const val MIN_X = -0.1f
        const val MAX_X = GameConfig.WORLD_WIDTH - 0.9f

        const val MIN_Y = 0.1f
        const val MAX_Y = GameConfig.WORLD_WIDTH - 2.9f

        const val MAX_SPEED = 0.12f
        const val SHOOT_TIMER = 0.2f

        const val START_Y = 1f

        const val MAX_ULTIMATE_WEAPON_COUNT = 3
    }

    var lives = GameConfig.LIVES_START
    var score = 0
    var ultimateWeapon = 2

    private val bound1 = Rectangle(0f, 0f, BOUNDS_VER_WIDTH, BOUNDS_VER_HEIGHT)
    private val bound2 = Rectangle(0f, 0f, BOUNDS_HOR_WIDTH, BOUNDS_HOR_HEIGHT)
    override val bounds = GdxArray<Rectangle>().apply { add(bound1, bound2) }

    override fun updateBounds() {
        bound1.setPosition(x + BOUNDS_VER_X_OFFSET, y + BOUNDS_VER_Y_OFFSET)
        bound2.setPosition(x + BOUNDS_HOR_X_OFFSET, y + BOUNDS_HOR_Y_OFFSET)
    }

    override fun canCollideWith(entity: EntityBase): Boolean {
        return entity.run {
            this is CivilianShip || this is SimpleEnemy || this is Collectible
        }
    }

    override fun isCollidingWith(entity: EntityBase): Boolean {
        if (!canCollideWith(entity)) {
            return false
        }
        return super.isCollidingWith(entity)
    }

    override fun onCollide(entity: EntityBase) {
        when (entity) {
            is CivilianShip -> {
                score += CivilianShip.SCORE_VALUE
                entity.toRemove = true
                damage(0.2f)
            }
            is SimpleEnemy -> {
                score += SimpleEnemy.SCORE_VALUE
                entity.toRemove = true
                damage(0.2f)
            }
        }
    }

    fun damage(amount: Float) {
        lives -= amount
        lives = round(lives * 100) / 100
        GameController.log.debug("PlayerHP: ${lives}")
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun textureWidth(): Float {
        return TEXTURE_WIDTH
    }

    override fun textureHeight(): Float {
        return TEXTURE_HEIGHT
    }
}