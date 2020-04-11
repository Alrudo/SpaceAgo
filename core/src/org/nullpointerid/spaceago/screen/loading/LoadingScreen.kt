package org.nullpointerid.spaceago.screen.loading

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.screen.game.GameScreen
import org.nullpointerid.spaceago.utils.clearScreen

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

        // load necessary stuff to AssetManager
        assetManager.load(AssetDescriptors.GAME_OVER_FONT)
        assetManager.load(AssetDescriptors.HALO_FONT)
        assetManager.load(AssetDescriptors.HUNGER_GAMES_FONT)
        assetManager.load(AssetDescriptors.SCORE_FONT)

        // blocks until all resources/assets are loaded
        assetManager.finishLoading()
    }

    override fun render(delta: Float) {
        update(delta)

        clearScreen()

        viewport.apply()
        renderer.projectionMatrix = camera.combined

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        draw()
        renderer.end()

        if (changeScreen) game.screen = GameScreen(game) // change screen when done.
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun hide() {
        // WARNING: Screens are not disposed automatically!
        // without dispose() call screen will be not disposed.
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
        val progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2 // center of screen on y

        renderer.rect(progressBarX, progressBarY, progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT)
    }
}