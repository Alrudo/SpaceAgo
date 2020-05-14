package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import org.nullpointerid.spaceago.SpaceShooter.GAME_ATLAS
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.collectables.Collectible
import org.nullpointerid.spaceago.entities.projectile.Bullet
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.utils.gdx.get
import org.nullpointerid.spaceago.views.game.GameController
import org.nullpointerid.spaceago.views.upgrade.UpgradeShopScreen
import kotlin.math.round

class Player(x: Float, y: Float, var name: String = "player1") : EntityBase(x, y, WIDTH, HEIGHT) {
    constructor() : this(0f, 0f)

    companion object {
        val TEXTURE = GAME_ATLAS[RegionNames.PLAYER]!!
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

    private var playerShootTimer = 0f
    @Transient var keyboardState: KeyboardState = KeyboardState()
    @Transient var upgradeState: UpgradeState = UpgradeState()
    set(value) {
        field = value
        lives = GameConfig.LIVES_START + 0.2f * upgradeState.getLevel(UpgradeShopScreen.Upgrades.DURABILITY)
    }
    var lives = GameConfig.LIVES_START + 0.2f * upgradeState.getLevel(UpgradeShopScreen.Upgrades.DURABILITY)
    var score = 0
    var ultimateWeapon = 2

    override val innerBounds = mutableListOf(
            XRectangle(0f, 0f, BOUNDS_1_WIDTH, BOUNDS_1_HEIGHT).bindToRect(coreBound, BOUNDS_1_X_OFFSET, BOUNDS_1_Y_OFFSET),
            XRectangle(0f, 0f, BOUNDS_2_WIDTH, BOUNDS_2_HEIGHT).bindToRect(coreBound, BOUNDS_2_X_OFFSET, BOUNDS_2_Y_OFFSET),
            XRectangle(0f, 0f, BOUNDS_3_WIDTH, BOUNDS_3_HEIGHT).bindToRect(coreBound, BOUNDS_3_X_OFFSET, BOUNDS_3_Y_OFFSET)
    )

    override fun update(delta: Float, world: World) {
        playerShootTimer += delta
        
        var xSpeed = 0f
        var ySpeed = 0f

        val speedBonus = upgradeState.getLevel(UpgradeShopScreen.Upgrades.MOVE_SPEED) * 0.4f
        if (keyboardState.isPressed("moveRight")) xSpeed = (MAX_SPEED  + speedBonus) * delta
        if (keyboardState.isPressed("moveLeft")) xSpeed = (-MAX_SPEED  - speedBonus) * delta
        if (keyboardState.isPressed("moveUp")) ySpeed = (MAX_SPEED  + speedBonus)* delta
        if (keyboardState.isPressed("moveDown")) ySpeed = (-MAX_SPEED  - speedBonus)* delta
        if (keyboardState.isPressed("shoot") &&
                playerShootTimer > (SHOOT_TIMER - upgradeState.getLevel(UpgradeShopScreen.Upgrades.ATTACK_SPEED) * 0.02f))
        {
            shoot(delta, world)
        }
//        if (Input.Keys.valueOf(ultimateWeapon).isKeyPressed() && player.ultimateWeapon > 0 && !laserBeam.used) {
//            player.ultimateWeapon--
//            laserBeam.lived = 0f
//            laserBeam.used = true
//        }

        x = MathUtils.clamp(x + xSpeed, MIN_X, MAX_X)
        y = MathUtils.clamp(y + ySpeed, MIN_Y, MAX_Y)
    }

    private fun shoot(delta: Float, world: World) {
        playerShootTimer = 0f
        when(upgradeState.getLevel(UpgradeShopScreen.Upgrades.SHOOTING)){
            0 -> {
                world.entities.add(Bullet(x, y + BOUNDS_1_HEIGHT + 0.15f, this))
            }
            1 -> {
                world.entities.add(Bullet(x - 0.05f, y + BOUNDS_1_HEIGHT, this).also { it.vector = Vector2(0f, 1f).rotate(1f).nor() })
                world.entities.add(Bullet(x + 0.05f, y + BOUNDS_1_HEIGHT, this).also { it.vector = Vector2(0f, 1f).rotate(-1f).nor() })
            }
            2 -> {
                world.entities.add(Bullet(x - 0.07f, y + BOUNDS_1_HEIGHT, this).also { it.vector = Vector2(0f, 1f).rotate(2f).nor() })
                world.entities.add(Bullet(x, y + BOUNDS_1_HEIGHT + 0.15f, this))
                world.entities.add(Bullet(x + 0.07f, y + BOUNDS_1_HEIGHT, this).also { it.vector = Vector2(0f, 1f).rotate(-2f).nor() })
            }
            3 -> {
                world.entities.add(Bullet(x - 0.17f, y - 0.07f, this).also { it.vector = Vector2(0f, 1f).rotate(4f).nor() })
                world.entities.add(Bullet(x - 0.07f, y + BOUNDS_1_HEIGHT, this).also { it.vector = Vector2(0f, 1f).rotate(2f).nor() })
                world.entities.add(Bullet(x, y + BOUNDS_1_HEIGHT + 0.15f, this))
                world.entities.add(Bullet(x + 0.07f, y + BOUNDS_1_HEIGHT, this).also { it.vector = Vector2(0f, 1f).rotate(-2f).nor() })
                world.entities.add(Bullet(x + 0.17f, y - 0.07f, this).also { it.vector = Vector2(0f, 1f).rotate(-4f).nor() })
            }
            4 -> {
                world.entities.add(Bullet(x - 0.21f, y - 0.07f, this).also { it.vector = Vector2(0f, 1f).rotate(9f).nor() })
                world.entities.add(Bullet(x - 0.17f, y - 0.07f, this).also { it.vector = Vector2(0f, 1f).rotate(4f).nor() })
                world.entities.add(Bullet(x - 0.07f, y + BOUNDS_1_HEIGHT, this).also { it.vector = Vector2(0f, 1f).rotate(2f).nor() })
                world.entities.add(Bullet(x, y + BOUNDS_1_HEIGHT + 0.15f, this))
                world.entities.add(Bullet(x + 0.07f, y + BOUNDS_1_HEIGHT, this).also { it.vector = Vector2(0f, 1f).rotate(-2f).nor() })
                world.entities.add(Bullet(x + 0.17f, y - 0.07f, this).also { it.vector = Vector2(0f, 1f).rotate(-4f).nor() })
                world.entities.add(Bullet(x + 0.21f, y - 0.07f, this).also { it.vector = Vector2(0f, 1f).rotate(-9f).nor() })
            }
        }
        Audio.shotSound.play(Audio.volume * 0.15f)
    }

    override fun updateBounds() {
        coreBound.setPosition(x - width / 2, y - height / 2)
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
}