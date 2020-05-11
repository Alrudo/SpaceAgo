package org.nullpointerid.spaceago.views.loading

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.views.menu.MenuScreen
import org.nullpointerid.spaceago.utils.Fonts
import org.nullpointerid.spaceago.utils.clearScreen
import org.nullpointerid.spaceago.utils.get
import java.awt.Font

class LoadingScreen(private val game: SpaceShooter) : ScreenAdapter() {

    companion object {
        private const val PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f // world units
        private const val PROGRESS_BAR_HEIGHT = 120f // world units
    }

    private val assetManager = game.assetManager
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var renderer: ShapeRenderer

    private var progress = 0f
    private var waitTime = 0.75f
    private var changeScreen = false // can't change screen without this variable.

    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
        renderer = ShapeRenderer()

        // load necessary assets to AssetManager
        assetManager.load(AssetDescriptors.MAIN_MENU_ATLAS)
        assetManager.load(AssetDescriptors.GAME_PLAY_ATLAS)
        assetManager.load(AssetDescriptors.SCORE_FONT)

        // blocks until all resources/assets are loaded
        assetManager.finishLoading()
        game.COMMON_SKIN.addRegions(assetManager[AssetDescriptors.MAIN_MENU_ATLAS])
        game.COMMON_SKIN.apply {
            add("halo", Fonts.HALO, BitmapFont::class.java)
            load(Gdx.files.internal("items/menu.json"))
        }
        game.COMMON_SKIN2.apply {
            load(Gdx.files.internal("items/keybind.json"))
        }
        game.background = assetManager[AssetDescriptors.MAIN_MENU_ATLAS][RegionNames.MENU_BACKGROUND]!!
    }

    override fun render(delta: Float) {
        update(delta)

        clearScreen()

        viewport.apply()
        renderer.projectionMatrix = camera.combined

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        draw()
        renderer.end()

        if (changeScreen) game.screen = MenuScreen(game) // change screen when done.
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        renderer.dispose()
    }

    // == private functions ==
    private fun update(delta: Float) {
        // progress is between 0 and 1
        progress = assetManager.progress

        // update returns true when all assets are loaded
        if (assetManager.update()) {
            waitTime -= delta

            if (waitTime <= 0) {
                changeScreen = true
            }
        }
    }

    private fun draw() {
        val progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f // center of screen on x
        val progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f // center of screen on y

        renderer.rect(progressBarX, progressBarY, progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT)
    }
}