package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import org.nullpointerid.spaceago.SpaceShooter.GAME_ATLAS
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.projectile.Bullet
import org.nullpointerid.spaceago.entities.projectile.EnemyBullet
import org.nullpointerid.spaceago.utils.XRectangle
import org.nullpointerid.spaceago.utils.gdx.get
import org.nullpointerid.spaceago.views.upgrade.UpgradeShopScreen
import kotlin.random.Random

class ShootingEnemy(x: Float, y: Float) : EntityBase(x, y, WIDTH, HEIGHT), Destroyable {
    constructor() : this(0f, 0f) {

    }

    companion object {
        val TEXTURE = GAME_ATLAS[RegionNames.SHOOTING_ENEMY]!!
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

        const val MAX_SPEED = 2f
        const val SHOOT_TIMER = 0.85f
    }

    override val innerBounds = mutableListOf(
            XRectangle(0f, 0f, BOUNDS_1_WIDTH, BOUNDS_1_HEIGHT).bindToRect(coreBound, BOUNDS_1_X_OFFSET, BOUNDS_1_Y_OFFSET),
            XRectangle(0f, 0f, BOUNDS_2_WIDTH, BOUNDS_2_HEIGHT).bindToRect(coreBound, BOUNDS_2_X_OFFSET, BOUNDS_2_Y_OFFSET),
            XRectangle(0f, 0f, BOUNDS_3_WIDTH, BOUNDS_3_HEIGHT).bindToRect(coreBound, BOUNDS_3_X_OFFSET, BOUNDS_3_Y_OFFSET)
    )

    @Transient
    private var playerShootTimer = 0f
    @Transient
    var rotationSpeed = Random.nextDouble(13.0, 39.0).toFloat() * Random.nextDouble(-1.0, 1.0).toFloat()
    @Transient
    var xSpeed = (Random.nextFloat() - 0.5f) * 2

    private var health: Float = getMaxHealth()

    init {
        coreBound.rotation = 180f
    }

    override fun update(delta: Float, world: World) {
        playerShootTimer += delta
        y -= MAX_SPEED * delta
        coreBound.rotation += rotationSpeed * delta

        if (playerShootTimer > (SHOOT_TIMER)){
            playerShootTimer = 0f
            world.entities.add(EnemyBullet(x, y).also {
                it.coreBound.rotation = coreBound.rotation
                it.vector = Vector2(0f, 1f).rotate(coreBound.rotation).nor()
            } )
        }
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun getMaxHealth(): Float {
        return 0.4f
    }

    override fun getHealth(): Float {
        return this.health
    }

    override fun setHealth(amount: Float) {
        this.health = amount
    }

    override fun getScore(): Int {
        return 250
    }
}

