package org.nullpointerid.spaceago.screen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import org.nullpointerid.spaceago.collectables.Collectible
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.isKeyPressed
import org.nullpointerid.spaceago.utils.logger
import org.nullpointerid.spaceago.utils.toInternalFile
import kotlin.math.round
import kotlin.random.Random

class GameController {

    companion object {
        @JvmStatic
        private val log = logger<GameController>()

        var moveRight  = "D"
        var moveLeft = "A"
        var moveUp = "W"
        var moveDown = "S"
        var shoot = "Space"

        const val BULLET_X = Player.BOUNDS_VER_WIDTH / 2f
        const val BULLET_Y = Player.BOUNDS_VER_HEIGHT

        const val EXPLOSION_X = (SimpleEnemy.BOUNDS_WIDTH - Explosion.TEXTURE_WIDTH) / 2f
        const val EXPLOSION_Y = (SimpleEnemy.BOUNDS_HEIGHT - Explosion.TEXTURE_HEIGHT) / 2f
    }

    private var simpleEnemyTimer = 0.15f + Random.nextFloat() * (0.50f - 0.15f)
    private var civilianShipTimer = 12f + Random.nextFloat() * (20f - 12f)
    private var playerShootTimer = 0f
    val simpleEnemies = GdxArray<SimpleEnemy>()
    val bullets = GdxArray<Bullet>()
    val explosions = GdxArray<Explosion>()
    val player = Player().apply { setPosition(2f, Player.START_Y) }
    val secondPlayer = Player().apply { setPosition(7f, Player.START_Y) }
    var civilianShips = GdxArray<CivilianShip>()
    val collectibles = GdxArray<EntityBase>()
    val laserBeam = LaserBeam(player)

    private val explosionSound = Gdx.audio.newSound("audio/enemyExplosionSound.mp3".toInternalFile())
    private val shootSound = Gdx.audio.newSound("audio/shotSound.mp3".toInternalFile())


    fun update(delta: Float) {
        if (player.lives <= 0f) return
        playerShootTimer += delta

        playerControl()
        blockPlayerFromLeavingTheWorld()
        spawnNewSimpleEnemy(delta)
        spawnNewCivilianShip(delta)
        updateEntities()
        checkForRemoval()

        if (isPlayerCollidingWithEntity(delta)) {
            player.lives -= 0.2f
            player.lives = round(player.lives * 100) / 100  // to fix the floating point errors.
            log.debug("PlayerHP: ${player.lives}")
        }

        if (laserBeam.used) {
            laserBeam.lived += delta
            if (laserBeam.lived >= LaserBeam.LIVE_TIME) laserBeam.used = false
            collidesWithLaser()
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
            bullets.add(Bullet(player).apply {
                setPosition(player.bounds[0].x + BULLET_X, player.bounds[0].y + BULLET_Y)
                shootSound.play(0.3f)
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

            civilianShips.add(civilianShip)
        }
    }

    private fun spawnNewSimpleEnemy(delta: Float) {
        simpleEnemyTimer -= delta

        if (simpleEnemyTimer <= 0) {
            simpleEnemyTimer = 0.15f + Random.nextFloat() * (0.50f - 0.15f)
            val enemyX = MathUtils.random(SimpleEnemy.MIN_X, SimpleEnemy.MAX_X)
            simpleEnemies.add(SimpleEnemy().apply { setPosition(enemyX, GameConfig.WORLD_HEIGHT) })
        }
    }

    private fun isPlayerCollidingWithEntity(delta: Float): Boolean {
        simpleEnemies.forEach {
            if (it.isCollidingWith(player)) {
                entityKilled(player, it)
                explosionSound.play(0.8f)
                return true
            } else if (it.y < -SimpleEnemy.BOUNDS_HEIGHT) { // remove enemy if outside the world bounds
                simpleEnemies.removeValue(it, true)
            }
        }

        civilianShips.forEach {
            if (it.isCollidingWith(player)) { // || it.isCollidingWith(secondPlayer)) {
                entityKilled(player, it)
                return true
            }
        }

        collectibles.forEach {
            if (it is Collectible) {
                it.lived += delta
                if (it.lived >= Collectible.MAX_LIFE_TIME) collectibles.removeValue(it, true)
                else if (player.isCollidingWith(it)) {
                    if (it.action(player)) collectibles.removeValue(it, true)
                }
            }
        }
        return false
    }

    private fun checkForRemoval() {
        bullets.forEach { bullet ->
            if (bullet.y > GameConfig.WORLD_HEIGHT + Bullet.BOUNDS_HEIGHT) {  // remove bullet if outside the world bounds
                bullets.removeValue(bullet, true)
            }
            simpleEnemies.forEach { enemy ->
                if (bullet.isCollidingWith(enemy)) {  // remove bullet and enemy if they collide.
                    bullets.removeValue(bullet, true)
                    entityKilled(bullet.owner as Player, enemy)
                }
            }
            civilianShips.forEach { civil ->
                if (bullet.isCollidingWith(civil)) {
                    bullets.removeValue(bullet, true)
                    civil.lives -= 0.1f
                    if (civil.lives <= 0f) {
                        entityKilled(bullet.owner as Player, civil)
                    }
                }
            }

            civilianShips.forEach {
                if (it.toLeft && it.x < -3f) {
                    civilianShips.removeValue(it, true)
                    log.debug("destroyed civilian ship at ${it.x} while it was moving to the left.")
                } else if (!it.toLeft && it.x > 13f) {
                    civilianShips.removeValue(it, true)
                    log.debug("destroyed civilian ship at ${it.x} while it was moving to the right.")
                }
            }
        }
    }

    private fun collidesWithLaser() {
        simpleEnemies.forEach {
            if (it.isCollidingWith(laserBeam)) entityKilled(laserBeam.owner, it)
        }

        civilianShips.forEach {
            if (it.isCollidingWith(laserBeam)) entityKilled(laserBeam.owner, it)
        }
    }

    private fun updateEntities() {
        simpleEnemies.forEach { it.update() }
        bullets.forEach { it.update() }
        civilianShips.forEach { it.update() }
        laserBeam.updateBounds()
    }

    private fun entityKilled(player: Player, entity: EntityBase) {
        if (entity is SimpleEnemy) {
            player.score += SimpleEnemy.SCORE_VALUE
            simpleEnemies.removeValue(entity, true)
            explosions.add(Explosion().apply { setPosition(entity.bounds[0].x + EXPLOSION_X, entity.bounds[0].y + EXPLOSION_Y) })
            explosionSound.play(0.8f)
            dropCollectible(entity)
        } else if (entity is CivilianShip) {
            player.score += CivilianShip.SCORE_VALUE
            civilianShips.removeValue(entity, true)
            if (entity.toLeft) explosions.add(Explosion().apply { setPosition(entity.bounds[1].x + entity.bounds[1].width / 2f, entity.bounds[1].y - 0.05f) })
            else explosions.add(Explosion().apply { setPosition(entity.bounds[0].x + entity.bounds[1].width / 2f, entity.bounds[0].y) })
            explosionSound.play(0.8f)
        }
    }

    private fun dropCollectible(entity: EntityBase) {
        if (entity is SimpleEnemy) {
            if (entity.containsDropable) {
                collectibles.add(Collectible.decideCollectible().apply {
                    setPosition(entity.bounds[0].x + entity.bounds[0].width / 2f,
                            entity.bounds[0].y + entity.bounds[0].height / 2f)
                })
            }
        }
    }
}
