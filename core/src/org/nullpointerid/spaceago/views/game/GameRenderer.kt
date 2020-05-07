package org.nullpointerid.spaceago.views.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter.gameAtlas
import org.nullpointerid.spaceago.assets.AssetPaths
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.entities.collectables.HealthPack
import org.nullpointerid.spaceago.entities.collectables.MoneyCrate
import org.nullpointerid.spaceago.entities.collectables.UltimateWeapon
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.entities.*
import org.nullpointerid.spaceago.utils.*

class GameRenderer(assetManager: AssetManager,
                   controller: GameController) : Disposable {

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

    private val background = gameAtlas[RegionNames.GAMEPLAY_BACKGROUND]
    private val playerTexture = gameAtlas[RegionNames.PLAYER]
    private val laserBeamTexture = gameAtlas[RegionNames.LASER_BEAM]
    private val font = BitmapFont(AssetPaths.SCORE_FONT.toInternalFile())
    private val gameFont = BitmapFont(Gdx.files.internal("fonts/gameFont.fnt"))

    private val player = controller.player
    private val secondPlayer = controller.secondPlayer
    private val entities = controller.entities
    private val laserBeam = controller.laserBeam


    fun render(delta: Float) {
        println(delta)
        maintainFPS(30)
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

            // player one hitboxes
            player.bounds.forEach {
                renderer.rectangle(it)
            }

            //player AI hitboxes
            secondPlayer.bounds.forEach {
                renderer.rectangle(it)
            }

            // Draw simpleEnemy hitboxes
            entities.forEach {
                it.bounds.forEach {bound ->
                    renderer.rectangle(bound)
                }
            }

            // Draw Laser Beam hitboxes.
            renderer.rectangle(laserBeam.bounds[0])
        }
        renderer.color = oldColor
    }

    private fun renderUi() {
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined

        batch.use {
            // Draw score text
            layout.setText(font, player.score.toString())
            font.draw(batch, layout, GameConfig.HUD_WIDTH / 2f - layout.width, GameConfig.HUD_HEIGHT - layout.height)

            // Draw Ultimate weapon text
            layout.setText(gameFont, "Ultimate weapon: ${player.ultimateWeapon}")
            gameFont.draw(batch, layout, 15f, 55f)
        }
    }

    private fun renderGameplay(delta: Float) {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        val oldColor = renderer.color.cpy()

        batch.use {
            // Draw background texture
            batch.draw(background, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)

            // Player one texture
            batch.color = Color.GREEN
            batch.draw(playerTexture, player.x, player.y, Player.TEXTURE_WIDTH, Player.TEXTURE_HEIGHT)

            // Player AI texture
            batch.color = Color.RED
            batch.draw(playerTexture, secondPlayer.x, secondPlayer.y, Player.TEXTURE_WIDTH, Player.TEXTURE_HEIGHT)
            batch.color = oldColor

            // Draw simpleEnemy texture
            entities.forEach {
                batch.draw(it.texture(), it.x, it.y, it.textureWidth(), it.textureHeight())
            }

            // Draw laser beam texture
            if (laserBeam.used) {
                batch.draw(laserBeamTexture, player.bounds[0].x - player.bounds[0].width / 2f + 0.01f,
                        player.bounds[0].y + player.bounds[0].height + 0.05f,
                        LaserBeam.TEXTURE_WIDTH, LaserBeam.TEXTURE_HEIGHT)
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