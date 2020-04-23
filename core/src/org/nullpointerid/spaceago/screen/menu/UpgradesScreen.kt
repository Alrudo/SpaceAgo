package org.nullpointerid.spaceago.screen.menu

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.clearScreen
import org.nullpointerid.spaceago.utils.drawGrid
import org.nullpointerid.spaceago.utils.get
import org.nullpointerid.spaceago.utils.use

class UpgradesScreen(private val assetManager: AssetManager) : Screen {

    private val renderer = ShapeRenderer()
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera()
    private val layout = GlyphLayout()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val menuAtlas = assetManager[AssetDescriptors.MAIN_MENU_ATLAS]
    private val background = menuAtlas[RegionNames.MENU_BACKGROUND]

    override fun render(delta: Float) {

        clearScreen()

        renderShop()

        if (GameConfig.DEBUG_MODE) {
            renderDebug()
            viewport.drawGrid(renderer, 100f)
        }
    }

    private fun renderDebug() {

    }

    private fun renderShop() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined
        batch.projectionMatrix = camera.combined

        batch.use {
            batch.draw(background, 0f, 0f)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
        renderer.dispose()
    }

    override fun hide() {
        dispose()
    }

    override fun pause() {}
    override fun show() {}
    override fun resume() {}
}