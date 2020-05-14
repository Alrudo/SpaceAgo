package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import org.nullpointerid.spaceago.SpaceShooter.GAME_ATLAS
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.entities.collectables.HealthPack
import org.nullpointerid.spaceago.entities.collectables.MoneyCrate
import org.nullpointerid.spaceago.entities.collectables.UltimateWeapon
import org.nullpointerid.spaceago.entities.projectile.EnemyBullet
import org.nullpointerid.spaceago.utils.XRectangle
import org.nullpointerid.spaceago.utils.gdx.get
import kotlin.random.Random

class RammingEnemy(x: Float, y: Float, @Transient var buff: Float = 1f) : EntityBase(x, y, WIDTH, HEIGHT), Destroyable {
    constructor() : this(0f, 0f)

    companion object {
        val TEXTURE = GAME_ATLAS[RegionNames.RAMMING_ENEMY]!!
        const val WIDTH = 0.8f
        const val HEIGHT = 0.8f
        private const val ration = WIDTH / 48

        const val BOUNDS_1_X_OFFSET = 16 * ration
        const val BOUNDS_1_Y_OFFSET = 24 * ration
        const val BOUNDS_1_WIDTH = 16 * ration
        const val BOUNDS_1_HEIGHT = 24 * ration

        const val BOUNDS_2_X_OFFSET = 0f
        const val BOUNDS_2_Y_OFFSET = 0f
        const val BOUNDS_2_WIDTH = 48 * ration
        const val BOUNDS_2_HEIGHT = 24 * ration

        const val MAX_SPEED = 8f
        const val SHOOT_TIMER = 0.95f
    }

    override val innerBounds = mutableListOf(
            XRectangle(0f, 0f, BOUNDS_1_WIDTH, BOUNDS_1_HEIGHT).bindToRect(coreBound, BOUNDS_1_X_OFFSET, BOUNDS_1_Y_OFFSET),
            XRectangle(0f, 0f, BOUNDS_2_WIDTH, BOUNDS_2_HEIGHT).bindToRect(coreBound, BOUNDS_2_X_OFFSET, BOUNDS_2_Y_OFFSET)
    )

    @Transient
    private var playerShootTimer = 0f

    @Transient
    private var health: Float = getMaxHealth()

    init {
        coreBound.rotation = 180f
    }

    @Transient
    var target: Player? = null

    var moveVector = Vector2(0f, -1f)
    var checkPoint: Boolean = false
    var checkPoint2: Float = 1f
    var checkPoint3: Float = 2f

    @ExperimentalStdlibApi
    override fun update(delta: Float, world: World) {
        playerShootTimer += delta

        if (!checkPoint) {
            y -= MAX_SPEED * 0.2f / Math.sqrt(buff.toDouble()).toFloat() * delta
            if (y <= 6.7f) checkPoint = true
            return
        } else if (checkPoint2 > 0f) {
            if (target == null) {
                target = world.entities.filter { it is Player && !it.isDead() }.randomOrNull() as Player?
            }

            if (target != null) {
                var rot = -(180.0 / Math.PI * Math.atan2((x - target!!.x).toDouble(), (y - target!!.y).toDouble()) - 180f).toFloat()
                coreBound.rotation = rot
                moveVector = Vector2(0f, 1f).rotate(rot).nor()
            }
            checkPoint2 -= delta
            return
        }else if (checkPoint3 > 0f) {
            checkPoint3 -= delta
        }
        x += moveVector.x *MAX_SPEED * delta
        y += moveVector.y *MAX_SPEED * delta
    }

    override fun onDestroy(world: World) {
        when (Random.nextInt(40)) {
            1 -> world.entities.add(HealthPack(x, y))
            2 -> world.entities.add(MoneyCrate(x, y))
            3 -> world.entities.add(UltimateWeapon(x, y))
        }
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun getMaxHealth(): Float {
        return 0.4f / buff
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

