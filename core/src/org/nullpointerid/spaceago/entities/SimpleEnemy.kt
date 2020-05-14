package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.nullpointerid.spaceago.SpaceShooter.GAME_ATLAS
import org.nullpointerid.spaceago.World
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.collectables.HealthPack
import org.nullpointerid.spaceago.entities.collectables.MoneyCrate
import org.nullpointerid.spaceago.entities.collectables.UltimateWeapon
import org.nullpointerid.spaceago.utils.gdx.get
import kotlin.random.Random

class SimpleEnemy(x: Float, y: Float) : EntityBase(x, y, WIDTH, HEIGHT), Destroyable {
    constructor() : this(0f, 0f) {

    }

    companion object {
        val TEXTURE = GAME_ATLAS[RegionNames.SIMPLE_ENEMY]!!
        const val WIDTH = 0.255f
        const val HEIGHT = 0.96f

        const val MIN_X = 0.12f
        const val MAX_X = GameConfig.WORLD_WIDTH - 0.12f

        const val MAX_SPEED = 2f
    }

    private var health: Float = getMaxHealth()
    var rotationSpeed = Random.nextDouble(13.0, 39.0).toFloat() * Random.nextDouble(-1.0, 1.0).toFloat()
    var xSpeed = (Random.nextFloat() - 0.5f) * 2

    override fun update(delta: Float, world: World) {
        y -= MAX_SPEED * delta
        x -= xSpeed * delta
        coreBound.rotation += rotationSpeed * delta
    }

    override fun texture(): TextureRegion {
        return TEXTURE
    }

    override fun onDestroy(world: World) {
        when (Random.nextInt(40)) {
            1 -> world.entities.add(HealthPack(x, y))
            2 -> world.entities.add(MoneyCrate(x, y))
            3 -> world.entities.add(UltimateWeapon(x, y))
        }
    }

    override fun getScore(): Int {
        return 35
    }

    override fun getMaxHealth(): Float {
        return 0.1f
    }

    override fun getHealth(): Float {
        return health
    }

    override fun setHealth(amount: Float) {
        health = amount
    }
}

