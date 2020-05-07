package org.nullpointerid.spaceago.views.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.isKeyPressed
import org.nullpointerid.spaceago.utils.logger
import org.nullpointerid.spaceago.views.gameover.GameOverScreen
import org.nullpointerid.spaceago.views.multiplayer.MultiplayerController
import kotlin.random.Random

class GameController(mpController: MultiplayerController?) {

    companion object {
        @JvmStatic
        val log = logger<GameController>()

        const val BULLET_X = Player.BOUNDS_VER_WIDTH / 2f
        const val BULLET_Y = Player.BOUNDS_VER_HEIGHT

        const val EXPLOSION_X = (SimpleEnemy.BOUNDS_WIDTH - Explosion.TEXTURE_WIDTH) / 2f
        const val EXPLOSION_Y = (SimpleEnemy.BOUNDS_HEIGHT - Explosion.TEXTURE_HEIGHT) / 2f
    }

    private var simpleEnemyTimer = 0.15f + Random.nextFloat() * (0.50f - 0.15f)
    private var civilianShipTimer = 12f + Random.nextFloat() * (20f - 12f)
    private var playerShootTimer = 0f
    val world = World()
    val secondPlayer = Player().apply { setPosition(7f, Player.START_Y) }
    val entities = GdxArray<EntityBase>()
    val player = Player().apply { setPosition(2f, Player.START_Y) }.also { entities.add(it) }
    val laserBeam = LaserBeam(player)


    fun update(delta: Float) {
        if (player.lives <= 0f) {
            SpaceShooter.screen = GameOverScreen(SpaceShooter.assetManager, player.score)
            return
        }
        playerShootTimer += delta

        playerControl()
        blockPlayerFromLeavingTheWorld()
        spawnNewSimpleEnemy(delta)
        spawnNewCivilianShip(delta)

        updateEntities(delta)
        checkCollisions()
        checkForRemoval()
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
            entities.add(Bullet(player).apply {
                setPosition(player.bounds[0].x + BULLET_X, player.bounds[0].y + BULLET_Y)
            })
        }
        if (Input.Keys.N.isKeyPressed() && player.ultimateWeapon > 0 && !laserBeam.used) {
            player.ultimateWeapon--
            laserBeam.lived = 0f
            laserBeam.used = true
        }

        player.x += xSpeed
        player.y += ySpeed
    }

    private fun spawnNewCivilianShip(delta: Float) {
        civilianShipTimer -= delta

        if (civilianShipTimer <= 0) {
            log.debug("Spawned new civilian ship.")
            civilianShipTimer = 12f + Random.nextFloat() * (20f - 12f)
            val coinToss = Random.nextInt(1, 101)
            val shipY = 1 + Random.nextFloat() * (6 - 1)

            val civilianShip = if (coinToss > 50) CivilianShip(true).apply { setPosition(11f, shipY) }
            else CivilianShip(false).apply { setPosition(-2.5f, shipY) }

            entities.add(civilianShip)
        }
    }

    private fun spawnNewSimpleEnemy(delta: Float) {
        simpleEnemyTimer -= delta

        if (simpleEnemyTimer <= 0) {
            simpleEnemyTimer = 0.15f + Random.nextFloat() * (0.50f - 0.15f)
            val enemyX = MathUtils.random(SimpleEnemy.MIN_X, SimpleEnemy.MAX_X)
            entities.add(SimpleEnemy().apply { setPosition(enemyX, GameConfig.WORLD_HEIGHT) })
        }
    }

    private fun checkCollisions() {
        entities.toList().forEach { entity ->
            entities.filter { entity != it }.forEach layer1@{ other ->
                if (!entity.isCollidingWith(other)) {
                    return@layer1
                }
                entity.onCollide(other)
            }
        }
    }

    private fun checkForRemoval() {
        entities.forEach {
            if (it.toRemove) {
                entities.removeValue(it, true)
                if(it is Destroyable){
                    entities.add(Explosion().apply { setPosition(it.bounds[0].x + EXPLOSION_X, it.bounds[0].y + EXPLOSION_Y) })
                }
            }
        }
    }

    private fun updateEntities(delta: Float) {
        entities.forEach { it.update(delta) }
        laserBeam.updateBounds()
    }
}
