package org.nullpointerid.spaceago.screen.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.AssetPaths
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.collectables.HealthPack
import org.nullpointerid.spaceago.collectables.MoneyCrate
import org.nullpointerid.spaceago.collectables.UltimateWeapon
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.entities.Bullet
import org.nullpointerid.spaceago.entities.Explosion
import org.nullpointerid.spaceago.entities.Player
import org.nullpointerid.spaceago.entities.SimpleEnemy
import org.nullpointerid.spaceago.utils.*

class GameRenderer(private val assetManager: AssetManager,
                   private val game: SpaceShooter,
                   private val controller: GameController) : Disposable {

    companion object {
        const val BULLET_OFFSET_X = -0.08f
        const val BULLET_OFFSET_Y = -0.03f
    }

    private val camera = OrthographicCamera()
    private val uiCamera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
    private val renderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()

    private val gameAtlas = assetManager[AssetDescriptors.GAME_PLAY_ATLAS]
    private val background = gameAtlas[RegionNames.GAMEPLAY_BACKGROUND]
    private val playerTexture = gameAtlas[RegionNames.PLAYER]
    private val simpleEnemyTexture = gameAtlas[RegionNames.SIMPLE_ENEMY]?.apply { flip(true, true) }
    private val bulletTexture = gameAtlas[RegionNames.BULLET]
    private val explosions = controller.explosions
    private val civilianShipToRight = gameAtlas[RegionNames.CIVILIAN_SHIP_RIGHT]
    private val civilianShipToLeft = gameAtlas[RegionNames.CIVILIAN_SHIP_LEFT]
    private val healthPack = Texture("images/raw/game_play/heathpack.png".toInternalFile())
    private val bulletCrate = Texture("images/raw/game_play/bullet_crate.png".toInternalFile())
    private val laserBeam = Texture("images/raw/game_play/laser_beam.png".toInternalFile())
    private val treasureChest = Texture("images/raw/game_play/treasure_chest.png".toInternalFile())

    private val font = BitmapFont(AssetPaths.SCORE_FONT.toInternalFile())
    private val player = controller.player
    private val secondPlayer = controller.secondPlayer
    private val simpleEnemies = controller.simpleEnemies
    private val bullets = controller.bullets
    private val civilianShips = controller.civilianShips
    private val collectibles = controller.collectibles

    fun render(delta: Float) {
        clearScreen()

        renderGameplay(delta)
        renderUi()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer)
            renderDebug()
        }
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined

        val oldColor = renderer.color.cpy()

        renderer.use {
            // Draw player hitboxes
            renderer.color = Color.GREEN

            // player one
            player.bounds.forEach {
                renderer.rectangle(it)
            }

            //player AI
            secondPlayer.bounds.forEach {
                renderer.rectangle(it)
            }

            // Draw simpleEnemy hitboxes
            simpleEnemies.forEach {
                renderer.rectangle(it.bounds[0])
            }

            // Draw civilian ship hitboxes
            civilianShips.forEach {
                it.bounds.forEach {bound ->
                    renderer.rectangle(bound)
                }
            }

            // Draw bullet hitboxes
            bullets.forEach {
                renderer.rectangle(it.bounds[0])
            }

            collectibles.forEach {
                renderer.rectangle(it.bounds[0])
            }

        }
        renderer.color = oldColor
    }

    private fun renderUi() {
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined

        batch.use {
            layout.setText(font, player.score.toString())

            font.draw(batch, layout, GameConfig.HUD_WIDTH / 2f - layout.width, GameConfig.HUD_HEIGHT - layout.height)
        }
    }

    private fun renderGameplay(delta: Float) {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        val oldColor = renderer.color.cpy()

        batch.use {
            // Draw background texture
            batch.draw(background, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)

            // Draw player texture
            // Player one.
            batch.color = Color.GREEN
            batch.draw(playerTexture, player.x, player.y, Player.TEXTURE_WIDTH, Player.TEXTURE_HEIGHT)

            // Player AI.
            batch.color = Color.RED
            batch.draw(playerTexture, secondPlayer.x, secondPlayer.y, Player.TEXTURE_WIDTH, Player.TEXTURE_HEIGHT)
            batch.color = oldColor

            // Draw simpleEnemy texture
            simpleEnemies.forEach {
                batch.draw(simpleEnemyTexture, it.x, it.y, SimpleEnemy.TEXTURE_WIDTH, SimpleEnemy.TEXTURE_HEIGHT)
            }

            collectibles.forEach {
                when(it) {
                    is MoneyCrate -> batch.draw(treasureChest, it.x, it.y, MoneyCrate.TEXTURE_WIDTH, MoneyCrate.TEXTURE_HEIGHT)
                    is HealthPack -> batch.draw(healthPack, it.x, it.y, HealthPack.TEXTURE_WIDTH, HealthPack.TEXTURE_HEIGHT)
                    is UltimateWeapon -> batch.draw(bulletCrate, it.x, it.y, UltimateWeapon.TEXTURE_WIDTH, UltimateWeapon.TEXTURE_HEIGHT)
                }
            }

            // Draw civilian ship texture.
            civilianShips.forEach {
                if (it.toLeft) {
                    batch.draw(civilianShipToLeft, it.x, it.y, CivilianShip.TEXTURE_WIDTH, CivilianShip.TEXTURE_HEIGH)
                } else {
                    batch.draw(civilianShipToRight, it.x, it.y, CivilianShip.TEXTURE_WIDTH, CivilianShip.TEXTURE_HEIGH)
                }
            }

            // Draw bullet texture
            bullets.forEach {
                batch.draw(bulletTexture, it.x + BULLET_OFFSET_X, it.y + BULLET_OFFSET_Y, Bullet.TEXTURE_WIDTH, Bullet.TEXTURE_HEIGHT)
            }

            // Draw explosion texture
            explosions.forEach {
                it.stateTime += delta
                batch.draw(it.animation.getKeyFrame(it.stateTime), it.x, it.y, Explosion.TEXTURE_WIDTH, Explosion.TEXTURE_HEIGHT)
                if (it.animation.isAnimationFinished(it.stateTime)) explosions.removeValue(it, true)
            }
        }

        // Draw player healthpoints
        renderer.projectionMatrix = camera.combined
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        when {
            player.lives < GameConfig.LIVES_START * 0.25f -> renderer.color = Color.RED
            player.lives < GameConfig.LIVES_START * 0.5f -> renderer.color = Color.ORANGE
            player.lives < GameConfig.LIVES_START * 0.75f -> renderer.color = Color.YELLOW
            else -> renderer.color = Color.GREEN
        }
        renderer.rect(0f, 0f, GameConfig.WORLD_WIDTH * (player.lives / GameConfig.LIVES_START), 0.2f)
        renderer.color = oldColor
        renderer.end()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun dispose() {}
}