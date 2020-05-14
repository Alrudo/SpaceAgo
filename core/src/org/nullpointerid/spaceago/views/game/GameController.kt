package org.nullpointerid.spaceago.views.game

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.utils.Audio
import org.nullpointerid.spaceago.utils.KeyboardState
import org.nullpointerid.spaceago.utils.XRectangle
import org.nullpointerid.spaceago.utils.gdx.logger
import org.nullpointerid.spaceago.views.gameover.GameOverScreen
import org.nullpointerid.spaceago.views.menu.MenuScreen
import org.nullpointerid.spaceago.views.multiplayer.MultiplayerController
import kotlin.random.Random

class GameController(var mpController: MultiplayerController?) {

    companion object {
        @JvmStatic
        val log = logger<GameController>()
    }


    private var sessionLength: Float = 0f
    private var simpleEnemyTimer = 0.1f + Random.nextFloat() * 0.2f
    private var civilianShipTimer = 7f + Random.nextFloat() * 8f
    private var playerShootTimer = 0f
    var keyboardState: KeyboardState
    var world = World()
        set(value) {
            field = value
            localPlayer = value.entities.find { it is Player && it.name == "player2" } as Player
        }
    var players: List<Player>
    var localPlayer: Player

    init {
        if (mpController != null) {
            mpController!!.game = this
            players = listOf(
                    Player(3f, Player.START_Y, "player1").also {
                        world.entities.add(it)
                        localPlayer = it
                        keyboardState = it.keyboardState
                    },
                    Player(6f, Player.START_Y, "player2").also {
                        world.entities.add(it)
                    }
            )
        } else {
            players = listOf(
                    Player(4f, Player.START_Y, "player1").also {
                        world.entities.add(it)
                        localPlayer = it
                        keyboardState = it.keyboardState
                    }
            )
        }
    }

    fun update(delta: Float) {
        sessionLength += delta
        if (players.all { it.lives <= 0f }) {
            mpController?.serverConnection?.sendTCP("GameOver:" + (players.find { it.name == "player2" }?.score ?: 0))
            SpaceShooter.screen = GameOverScreen(localPlayer.score)
            mpController?.closeSocket()
            return
        }
        playerShootTimer += delta

        spawnNewEnemy(delta)
        spawnNewCivilianShip(delta)

        updateEntities(delta)
        checkCollisions()
        checkForRemoval()
        keyboardState.update()
    }

    fun updateClient(delta: Float) {
        keyboardState.update()
        try {
            mpController!!.clientConnection!!.sendTCP(keyboardState)
        } catch (e: Exception) {
            SpaceShooter.screen = MenuScreen()
        }
    }

    private fun spawnNewCivilianShip(delta: Float) {
        civilianShipTimer -= delta

        if (civilianShipTimer <= 0) {
            log.debug("Spawned new civilian ship.")
            civilianShipTimer = 12f + Random.nextFloat() * (20f - 12f)
            val coinToss = Random.nextInt(1, 101)
            val shipY = 1 + Random.nextFloat() * (6 - 1)

            val civilianShip = if (coinToss > 50) CivilianShip(11f, shipY, true)
            else CivilianShip(-2.5f, shipY, false)

            world.entities.add(civilianShip)
        }
    }

    private fun spawnNewEnemy(delta: Float) {
        simpleEnemyTimer -= delta

        when{
            sessionLength<20 -> {
                if (simpleEnemyTimer <= 0) {
                    simpleEnemyTimer = 0.25f + Random.nextFloat() * 0.45f
                    val enemyX = MathUtils.random(SimpleEnemy.MIN_X, SimpleEnemy.MAX_X)
                    when (Random.nextInt(5)) {
                        0 -> world.entities.add(ShootingEnemy(enemyX, GameConfig.WORLD_HEIGHT))
                        else -> world.entities.add(SimpleEnemy(enemyX, GameConfig.WORLD_HEIGHT))
                    }
                }
            }
            sessionLength<60 -> {
                if (simpleEnemyTimer <= 0) {
                    simpleEnemyTimer = 0.25f + Random.nextFloat() * 0.45f
                    val enemyX = MathUtils.random(SimpleEnemy.MIN_X, SimpleEnemy.MAX_X)
                    when (Random.nextInt(8)) {
                        in 0..3 -> world.entities.add(ShootingEnemy(enemyX, GameConfig.WORLD_HEIGHT))
                        else -> world.entities.add(SimpleEnemy(enemyX, GameConfig.WORLD_HEIGHT))
                    }
                }
            }
            sessionLength<120 -> {
                if (simpleEnemyTimer <= 0) {
                    simpleEnemyTimer = 0.20f + Random.nextFloat() * 0.35f
                    val enemyX = MathUtils.random(SimpleEnemy.MIN_X, SimpleEnemy.MAX_X)
                    when (Random.nextInt(10)) {
                        in 0..4 -> world.entities.add(ShootingEnemy(enemyX, GameConfig.WORLD_HEIGHT, 0.8f))
                        5 -> world.entities.add(RammingEnemy(enemyX, GameConfig.WORLD_HEIGHT))
                        else -> world.entities.add(SimpleEnemy(enemyX, GameConfig.WORLD_HEIGHT))
                    }
                }
            }
            sessionLength<240 -> {
                if (simpleEnemyTimer <= 0) {
                    simpleEnemyTimer = 0.10f + Random.nextFloat() * 0.25f
                    val enemyX = MathUtils.random(SimpleEnemy.MIN_X, SimpleEnemy.MAX_X)
                    when (Random.nextInt(12)) {
                        in 0..5 -> world.entities.add(ShootingEnemy(enemyX, GameConfig.WORLD_HEIGHT, 0.5f))
                        in 5..7 -> world.entities.add(RammingEnemy(enemyX, GameConfig.WORLD_HEIGHT, 0.5f))
                        else -> world.entities.add(SimpleEnemy(enemyX, GameConfig.WORLD_HEIGHT))
                    }
                }
            }
            else -> {

            }
        }
    }

    private fun checkCollisions() {
        world.entities.toList().forEach { entity ->
            world.entities.filter { entity != it }.forEach layer1@{ other ->
                if (!entity.isCollidingWith(other)) {
                    return@layer1
                }
                entity.onCollide(other)
            }
        }
    }


    val x: XRectangle = XRectangle(-1f, -1f, 12f, 12f)
    private fun checkForRemoval() {
        world.entities.toSet().forEach {
            if (it.toRemove) {
                it.onDestroy(world)
                if (it is Destroyable) {
                    world.entities.add(Explosion(it.x, it.y))
                    Audio.explosionSound.play(Audio.volume * 0.1f)
                }
                world.entities.remove(it)
            }
            if (!Intersector.overlapConvexPolygons(it.coreBound, x)) {
                world.entities.remove(it)
            }
        }
    }

    private fun updateEntities(delta: Float) {
        world.entities.toList().forEach { it.update(delta, world) }
    }
}
