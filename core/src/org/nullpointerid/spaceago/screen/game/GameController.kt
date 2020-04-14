package org.nullpointerid.spaceago.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pools
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.entities.SimpleEnemy
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.isKeyPressed
import org.nullpointerid.spaceago.utils.logger
import kotlin.random.Random

class GameController {

    companion object {
        @JvmStatic
        private val log = logger<GameController>()
    }

    private var simpleEnemyTimer = Random.nextFloat() * (GameConfig.MAX_ENEMY_SPAWN_TIME - GameConfig.MIN_ENEMY_SPAWN_TIME) - GameConfig.MIN_ENEMY_SPAWN_TIME
    val simpleEnemy = SimpleEnemy().apply { setPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)}
    val simpleEnemies = GdxArray<SimpleEnemy>()
    //    private val simpleEnemyPool = Pools.get(SimpleEnemy::class.java)
    val player = Player().apply { setPosition(Player.START_X, Player.START_Y) }
    var score = 0


    fun update(delta: Float) {

        movePlayer()
        blockPlayerFromLeavingTheWorld()
        spawnNewSimpleEnemy(delta)
        updateEnemies()

        if (isPlayerCollidingWithEntity()) {
            log.debug("Collision detected!")
            player.lives -= 0.1f
        }
    }

    private fun blockPlayerFromLeavingTheWorld() {
        player.x = MathUtils.clamp(player.x, Player.MIN_X, Player.MAX_X)
        player.y = MathUtils.clamp(player.y, Player.MIN_Y, Player.MAX_Y)
    }

    private fun movePlayer() {
        var xSpeed = 0f
        var ySpeed = 0f

        if (Input.Keys.D.isKeyPressed()) xSpeed = Player.MAX_SPEED
        if (Input.Keys.A.isKeyPressed()) xSpeed = -Player.MAX_SPEED
        if (Input.Keys.W.isKeyPressed()) ySpeed = Player.MAX_SPEED
        if (Input.Keys.S.isKeyPressed()) ySpeed = -Player.MAX_SPEED

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

    private fun updateEnemies() = simpleEnemies.forEach { it.update() }
}