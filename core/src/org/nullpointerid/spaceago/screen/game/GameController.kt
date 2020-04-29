package org.nullpointerid.spaceago.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import org.nullpointerid.spaceago.collectables.Collectible
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.isKeyPressed
import org.nullpointerid.spaceago.utils.logger
import kotlin.math.round
import kotlin.random.Random

class GameController {

    companion object {
        @JvmStatic
        private val log = logger<GameController>()

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
            })
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
                simpleEnemies.removeValue(it, true)
                explosions.add(Explosion().apply { setPosition(it.bounds[0].x + EXPLOSION_X, it.bounds[0].y + EXPLOSION_Y) })
                if (it.containsDropable) {
                    collectibles.add(Collectible.decideCollectible().apply {
                        setPosition(it.bounds[0].x + it.bounds[0].width / 2f,
                                it.bounds[0].y + it.bounds[0].height / 2f)
                    })
                }
                player.score += 100
                return true
            } else if (it.y < -SimpleEnemy.BOUNDS_HEIGHT) { // remove enemy if outside the world bounds
                simpleEnemies.removeValue(it, true)
            }
        }

        civilianShips.forEach {
            if (it.isCollidingWith(player)) { // || it.isCollidingWith(secondPlayer)) {
                civilianShips.removeValue(it, true)
                if (it.toLeft) explosions.add(Explosion().apply { setPosition(it.bounds[1].x + it.bounds[1].width / 2f, it.bounds[1].y - 0.05f) })
                else explosions.add(Explosion().apply { setPosition(it.bounds[0].x + it.bounds[1].width / 2f, it.bounds[0].y) })
                player.score -= 500 // if (it.isCollidingWith(player)) player.score -= 500
//                else if (it.isCollidingWith(secondPlayer)) secondPlayer.score -= 500
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
                    simpleEnemies.removeValue(enemy, true)
                    explosions.add(Explosion().apply { setPosition(enemy.bounds[0].x + EXPLOSION_X, enemy.bounds[0].y + EXPLOSION_Y) })
                    if (bullet.owner is Player) bullet.owner.score += 100
                    bullets.removeValue(bullet, true)
                    if (enemy.containsDropable) {
                        collectibles.add(Collectible.decideCollectible().apply {
                            setPosition(enemy.bounds[0].x + enemy.bounds[0].width / 2f,
                                    enemy.bounds[0].y + enemy.bounds[0].height / 2f)
                        })
                    }
                }
            }
            civilianShips.forEach { civil ->
                if (bullet.isCollidingWith(civil)) {
                    bullets.removeValue(bullet, true)
                    civil.lives -= 0.1f
                    if (civil.lives <= 0f) {
                        civilianShips.removeValue(civil, true)
                        if (civil.toLeft) explosions.add(Explosion().apply { setPosition(civil.bounds[1].x + civil.bounds[1].width / 2f, civil.bounds[1].y - 0.05f) })
                        else explosions.add(Explosion().apply { setPosition(civil.bounds[0].x + civil.bounds[1].width / 2f, civil.bounds[0].y) })
                        if (bullet.owner is Player) bullet.owner.score -= 500
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

    private fun updateEntities() {
        simpleEnemies.forEach { it.update() }
        bullets.forEach { it.update() }
        civilianShips.forEach { it.update() }
    }
}