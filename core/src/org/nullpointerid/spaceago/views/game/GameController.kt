package org.nullpointerid.spaceago.views.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.utils.isKeyPressed
import org.nullpointerid.spaceago.utils.logger
import org.nullpointerid.spaceago.views.upgrade.UpgradeShopScreen
import org.nullpointerid.spaceago.utils.toInternalFile
import org.nullpointerid.spaceago.views.gameover.GameOverScreen
import org.nullpointerid.spaceago.views.multiplayer.MultiplayerController
import kotlin.random.Random

class GameController(var mpController: MultiplayerController?) {

    companion object {
        @JvmStatic
        val log = logger<GameController>()
        const val volumeSuppress = 0.5f
    }

    private val prefs = Gdx.app.getPreferences("spaceshooter")

    private val volume = prefs.getFloat("volume", 0.5f)
    private val moveUp = prefs.getString("moveUp", "W")
    private val moveDown = prefs.getString("moveDown", "S")
    private val moveLeft = prefs.getString("moveLeft", "A")
    private val moveRight = prefs.getString("moveRight", "D")
    private val shoot = prefs.getString("shoot", "Space")
    private val ultimateWeapon = prefs.getString("ultimate", "N")

    private val moveSpeedUpgrade = prefs.getInteger(UpgradeShopScreen.Upgrades.MOVE_SPEED.toString(), 0)
    private val attackSpeedUpgrade = prefs.getInteger(UpgradeShopScreen.Upgrades.ATTACK_SPEED.toString(), 0)
    private val durabilityUpgrade = prefs.getInteger(UpgradeShopScreen.Upgrades.DURABILITY.toString(), 0)

    private var simpleEnemyTimer = 0.15f + Random.nextFloat() * (0.50f - 0.15f)
    private var civilianShipTimer = 2f
    private var playerShootTimer = 0f
    var world = World()
    val player = Player(2f, Player.START_Y).also { world.entities.add(it) }
    var player2: Player? = null
    private var shotSound: Sound = Gdx.audio.newSound("audio/shotSound.mp3".toInternalFile())
    private var explosionSound: Sound = Gdx.audio.newSound("audio/enemyExplosionSound.mp3".toInternalFile())

    init {
        if (mpController != null) {
            mpController!!.game = this
            player2 = Player(6f, Player.START_Y, "player2").also { world.entities.add(it) }
        }
    }

    fun update(delta: Float) {
        if (player.lives <= 0f) {
            SpaceShooter.screen = GameOverScreen(SpaceShooter.assetManager, player.score)
            return
        }
        playerShootTimer += delta

        playerControl(delta)
        blockPlayerFromLeavingTheWorld()
        spawnNewSimpleEnemy(delta)
        spawnNewCivilianShip(delta)

        updateEntities(delta)
        checkCollisions()
        checkForRemoval()
    }

    fun updateClient(delta: Float) {
        if (player2 == null) {
            return
        }

        playerShootTimer += delta

        var xSpeed = 0f
        var ySpeed = 0f

        if (Input.Keys.D.isKeyPressed()) xSpeed = Player.MAX_SPEED * delta
        if (Input.Keys.A.isKeyPressed()) xSpeed = -Player.MAX_SPEED * delta
        if (Input.Keys.W.isKeyPressed()) ySpeed = Player.MAX_SPEED * delta
        if (Input.Keys.S.isKeyPressed()) ySpeed = -Player.MAX_SPEED * delta
        if (Input.Keys.SPACE.isKeyPressed() && playerShootTimer > Player.SHOOT_TIMER) {
            playerShootTimer = 0f
            world.entities.add(Bullet(player2!!.x, player2!!.y, player2))
        }

        println("shooting $xSpeed $ySpeed $")
        mpController!!.clientConnection!!.sendTCP(player2)
        player2!!.x += xSpeed
        player2!!.y += ySpeed
    }

    private fun blockPlayerFromLeavingTheWorld() {
        player.x = MathUtils.clamp(player.x, Player.MIN_X, Player.MAX_X)
        player.y = MathUtils.clamp(player.y, Player.MIN_Y, Player.MAX_Y)
    }

    private fun playerControl(delta: Float) {
        var xSpeed = 0f
        var ySpeed = 0f

        if (Input.Keys.valueOf(moveRight).isKeyPressed()) xSpeed = (Player.MAX_SPEED  + moveSpeedUpgrade.toFloat() * 0.02f) * delta
        if (Input.Keys.valueOf(moveLeft).isKeyPressed()) xSpeed = (-Player.MAX_SPEED  - moveSpeedUpgrade.toFloat() * 0.02f) * delta
        if (Input.Keys.valueOf(moveUp).isKeyPressed()) ySpeed = (Player.MAX_SPEED  + moveSpeedUpgrade.toFloat() * 0.02f)* delta
        if (Input.Keys.valueOf(moveDown).isKeyPressed()) ySpeed = (-Player.MAX_SPEED  - moveSpeedUpgrade.toFloat() * 0.02f)* delta
        if (Input.Keys.valueOf(shoot).isKeyPressed() && playerShootTimer > (Player.SHOOT_TIMER - attackSpeedUpgrade.toFloat() * 0.02f)) {
            playerShootTimer = 0f
            world.entities.add(Bullet(player.x, player.y, player))
            shotSound.play(volume * 0.3f * volumeSuppress)
        }
//        if (Input.Keys.valueOf(ultimateWeapon).isKeyPressed() && player.ultimateWeapon > 0 && !laserBeam.used) {
//            player.ultimateWeapon--
//            laserBeam.lived = 0f
//            laserBeam.used = true
//        }

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

            val civilianShip = if (coinToss > 50) CivilianShip(11f, shipY, true)
            else CivilianShip(-2.5f, shipY, false)

            world.entities.add(civilianShip)
        }
    }

    private fun spawnNewSimpleEnemy(delta: Float) {
        simpleEnemyTimer -= delta

        if (simpleEnemyTimer <= 0) {
            simpleEnemyTimer = 0.15f + Random.nextFloat() * (0.50f - 0.15f)
            val enemyX = MathUtils.random(SimpleEnemy.MIN_X, SimpleEnemy.MAX_X)
            world.entities.add(SimpleEnemy(enemyX, GameConfig.WORLD_HEIGHT))
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

    private fun checkForRemoval() {
        world.entities.toSet().forEach {
            if (it.toRemove) {
                if (it is Destroyable) {
                    world.entities.add(Explosion(it.x, it.y))
                }
                world.entities.remove(it)
            }
        }
    }

    private fun updateEntities(delta: Float) {
        world.entities.forEach { it.update(delta) }
    }
}
