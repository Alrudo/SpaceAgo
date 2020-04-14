package org.nullpointerid.spaceago.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.Bullet
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.entities.SimpleEnemy
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.isKeyPressed
import org.nullpointerid.spaceago.utils.logger
import kotlin.random.Random

class GameController {

    private var simpleEnemyTimer = Random.nextFloat() * (GameConfig.MAX_ENEMY_SPAWN_TIME - GameConfig.MIN_ENEMY_SPAWN_TIME) + GameConfig.MIN_ENEMY_SPAWN_TIME
    private var playerShootTimer = 0f
    val simpleEnemies = GdxArray<SimpleEnemy>()
    val bullets = GdxArray<Bullet>()
    val player = Player().apply { setPosition(Player.START_X, Player.START_Y) }
    var score = 0


    fun update(delta: Float) {
        playerShootTimer += delta

        playerControl()
        blockPlayerFromLeavingTheWorld()
        spawnNewSimpleEnemy(delta)
        updateEnemies()
        updateBullets()
        isBulletCollidingWithEntity()
        destroyEntitiesOutside()

        if (isPlayerCollidingWithEntity()) {
            player.lives -= 0.1f
        }
    }

    private fun blockPlayerFromLeavingTheWorld() {
        player.x = MathUtils.clamp(player.x, Player.MIN_X, Player.MAX_X)
        player.y = MathUtils.clamp(player.y, Player.MIN_Y, Player.MAX_Y)
    }

    private fun playerControl() {
        var xSpeed = 0f
        var ySpeed = 0f

        if (Input.Keys.D.isKeyPressed()) xSpeed = Player.MAX_SPEED
        if (Input.Keys.A.isKeyPressed()) xSpeed = -Player.MAX_SPEED
        if (Input.Keys.W.isKeyPressed()) ySpeed = Player.MAX_SPEED
        if (Input.Keys.S.isKeyPressed()) ySpeed = -Player.MAX_SPEED
        if (Input.Keys.SPACE.isKeyPressed() && playerShootTimer > Player.SHOOT_TIMER) {
            playerShootTimer = 0f
            bullets.add(Bullet().apply {
                setPosition(player.x + Bullet.OFFSET_X_PLAYER, player.y + Bullet.OFFSET_Y_PLAYER)
            })
        }

        player.x += xSpeed
        player.y += ySpeed
    }

    private fun spawnNewSimpleEnemy(delta: Float) {
        simpleEnemyTimer -= delta

        if (simpleEnemyTimer <= 0) {
            simpleEnemyTimer = Random.nextFloat() * (GameConfig.MAX_ENEMY_SPAWN_TIME - GameConfig.MIN_ENEMY_SPAWN_TIME) + GameConfig.MIN_ENEMY_SPAWN_TIME
            val enemyX = MathUtils.random(SimpleEnemy.MIN_X, SimpleEnemy.MAX_X)
            simpleEnemies.add(SimpleEnemy().apply { setPosition(enemyX, GameConfig.WORLD_HEIGHT) })
        }
    }

    private fun isPlayerCollidingWithEntity(): Boolean {
        simpleEnemies.forEach {
            if (it.isCollidingWith(player)) {
                simpleEnemies.removeValue(it, true)
                score += 100
                return true
            }
        }
        return false
    }

    private fun isBulletCollidingWithEntity() {
        bullets.forEach { bullet ->
            simpleEnemies.forEach { enemy ->
                if (bullet.isCollidingWith(enemy)) {
                    bullets.removeValue(bullet, true)
                    simpleEnemies.removeValue(enemy, true)
                    score += 100
                }
            }
        }
    }

    private fun destroyEntitiesOutside() {
        // Can be united with "isPlayerCollidingWithEntity()"
        simpleEnemies.forEach {
            if (it.y < -SimpleEnemy.BOUNDS_HEIGHT) simpleEnemies.removeValue(it, true)
        }
        bullets.forEach {
            if (it.y > GameConfig.WORLD_HEIGHT + Bullet.BOUNDS_HEIGHT) bullets.removeValue(it, true)
        }
    }

    private fun updateEnemies() = simpleEnemies.forEach { it.update() }
    private fun updateBullets() = bullets.forEach { it.update() }
}