package org.nullpointerid.spaceago.views.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter.GAME_ATLAS
import org.nullpointerid.spaceago.SpaceShooter.STORAGE
import org.nullpointerid.spaceago.assets.AssetPaths
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.utils.gdx.*
import org.nullpointerid.spaceago.views.upgrade.UpgradeShopScreen

class GameRenderer(var controller: GameController) : Disposable {

    private val durabilityUpgrade = STORAGE.getInteger(UpgradeShopScreen.Upgrades.DURABILITY.toString(), 0)

    private val camera = OrthographicCamera()
    private val uiCamera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
    private val renderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val layout = GlyphLayout()

    private val background = GAME_ATLAS[RegionNames.GAMEPLAY_BACKGROUND]
    private val font = BitmapFont(AssetPaths.SCORE_FONT.toInternalFile())
    private val gameFont = BitmapFont(Gdx.files.internal("fonts/gameFont.fnt"))



    fun render(delta: Float) {
        clearScreen()

        renderGameplay()
        renderUi()

        if (GameConfig.DEBUG_MODE) renderDebug()

    }

    private fun renderDebug() {
        viewport.apply()
        viewport.drawGrid(renderer)
        renderer.projectionMatrix = camera.combined

        val oldColor = renderer.color.cpy()

        renderer.use {
            controller.world.entities.forEach {
                renderer.color = Color.ROYAL
                renderer.polygon(it.coreBound.transformedVertices)
                renderer.color = Color.GREEN

                it.innerBounds.forEach { bound ->
                    renderer.polygon(bound.transformedVertices)
                }
            }
        }
        renderer.color = oldColor
    }

    private fun renderUi() {
        uiViewport.apply()
        batch.projectionMatrix = uiCamera.combined

        batch.use {
            // Draw score text
            layout.setText(font, controller.localPlayer.score.toString())
            font.draw(batch, layout, GameConfig.HUD_WIDTH / 2f - layout.width, GameConfig.HUD_HEIGHT - layout.height)

            // Draw Ultimate weapon text
            layout.setText(gameFont, "Ultimate weapon: ${controller.localPlayer.ultimateWeapon}")
            gameFont.draw(batch, layout, 15f, 55f)
        }
    }

    private fun renderGameplay() {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        batch.use {
            batch.draw(background, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)
            controller.world.entities.forEach {
                batch.draw(it)
            }
        }

        val oldColor = renderer.color.cpy()
        // Draw player healthpoints
        renderer.projectionMatrix = camera.combined
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        when {
            controller.localPlayer.lives < GameConfig.LIVES_START * 0.25f -> renderer.color = Color.RED
            controller.localPlayer.lives < GameConfig.LIVES_START * 0.5f -> renderer.color = Color.ORANGE
            controller.localPlayer.lives < GameConfig.LIVES_START * 0.75f -> renderer.color = Color.YELLOW
            else -> renderer.color = Color.GREEN
        }
        renderer.rect(0f, 0f, GameConfig.WORLD_WIDTH * (controller.localPlayer.lives / (GameConfig.LIVES_START + durabilityUpgrade.toFloat() * 0.1f)), 0.2f)
        renderer.color = oldColor
        renderer.end()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun dispose() {}
}