package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter.gameAtlas
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.collectables.Collectible
import org.nullpointerid.spaceago.utils.XRectangle
import org.nullpointerid.spaceago.utils.get
import org.nullpointerid.spaceago.views.game.GameController
import kotlin.math.round

class Player(x: Float, y: Float, var name: String = "player1") : EntityBase(x, y, WIDTH, HEIGHT) {
    constructor() : this(0f, 0f)

    companion object {
        val TEXTURE = gameAtlas[RegionNames.PLAYER]!!
        const val WIDTH = 0.8f
        const val HEIGHT = 0.8f
        private const val ration = WIDTH / 48

        const val BOUNDS_1_X_OFFSET = 19 * ration
        const val BOUNDS_1_Y_OFFSET = 27 * ration
        const val BOUNDS_1_WIDTH = 10 * ration
        const val BOUNDS_1_HEIGHT = 21 * ration

        const val BOUNDS_2_X_OFFSET = 0f
        const val BOUNDS_2_Y_OFFSET = 14 * ration
        const val BOUNDS_2_WIDTH = 48 * ration
        const val BOUNDS_2_HEIGHT = 13 * ration

        const val BOUNDS_3_X_OFFSET = 10 * ration
        const val BOUNDS_3_Y_OFFSET = 0 * ration
        const val BOUNDS_3_WIDTH = 28 * ration
        const val BOUNDS_3_HEIGHT = 14 * ration

        const val MIN_X = 0.3f
        const val MAX_X = GameConfig.WORLD_WIDTH - 0.3f

        const val MIN_Y = 0.3f
        const val MAX_Y = GameConfig.WORLD_WIDTH - 2.9f

        const val MAX_SPEED = 5f
        const val SHOOT_TIMER = 0.2f

        const val START_Y = 1f

        const val MAX_ULTIMATE_WEAPON_COUNT = 3
    }

    var lives = GameConfig.LIVES_START
    var score = 0
    var ultimateWeapon = 2

    override val innerBounds = mutableListOf(
            XRectangle(0f, 0f, BOUNDS_1_WIDTH, BOUNDS_1_HEIGHT).bindToRect(coreBound, BOUNDS_1_X_OFFSET, BOUNDS_1_Y_OFFSET),
            XRectangle(0f, 0f, BOUNDS_2_WIDTH, BOUNDS_2_HEIGHT).bindToRect(coreBound, BOUNDS_2_X_OFFSET, BOUNDS_2_Y_OFFSET),
            XRectangle(0f, 0f, BOUNDS_3_WIDTH, BOUNDS_3_HEIGHT).bindToRect(coreBound, BOUNDS_3_X_OFFSET, BOUNDS_3_Y_OFFSET)
    )

    override fun updateBounds() {
        coreBound.setPosition(x - textureWidth() / 2, y - textureHeight() / 2)
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
        GameController.log.debug("PlayerHP: $lives")
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun textureWidth(): Float {
        return WIDTH
    }

    override fun textureHeight(): Float {
        return HEIGHT
    }
}