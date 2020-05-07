package org.nullpointerid.spaceago.views.gameover

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.views.game.GameScreen
import org.nullpointerid.spaceago.views.menu.MenuScreen
import org.nullpointerid.spaceago.utils.*

class GameOverScreen(assetManager: AssetManager,
                     private val score: Int) : Screen {

    companion object {
        @JvmStatic
        private val log = logger<GameOverScreen>()
    }

    private val prefs = Gdx.app.getPreferences("spaceshooter")
    private val highscore = prefs.getInteger("highscore", 0)
    private val currentCash = prefs.getInteger("money", 0)
    private val menuHitboxes = Rectangle(150f, 150f, 300f, 100f)
    private val retryHitboxes = Rectangle(600f, 150f, 200f, 100f)
    private var changeToMenu = false
    private var retry = false

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)
    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val layout = GlyphLayout()

    private val gameAtlas = assetManager[AssetDescriptors.GAME_PLAY_ATLAS]
    private val gameOverAtlas = assetManager[AssetDescriptors.GAME_OVER_ATLAS]
    private val background = gameAtlas[RegionNames.GAMEPLAY_BACKGROUND]
    private val gameOver = gameOverAtlas[RegionNames.GAME_OVER]
    private val scoreFont = assetManager[AssetDescriptors.SCORE_FONT]
    private val haloFont = FreeTypeFontGenerator("fonts/Halo3.ttf".toInternalFile()).generateFont(
            FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                size = 75
                borderWidth = 5f
                borderColor = Color.BLACK
                color = Color.GRAY
            })

    override fun show() {
        if (score > highscore) {
            prefs.putInteger("highscore", score)
        }
        prefs.putInteger("money", currentCash + score / 100)
        prefs.flush()
        log.debug("${prefs.getInteger("money")}")
    }

    override fun render(delta: Float) {
        clearScreen()

        renderMenu()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderDebug()
        }

        if (changeToMenu) SpaceShooter.screen = MenuScreen()
        if (retry) SpaceShooter.screen = GameScreen()
    }

    private fun renderDebug() {
        viewport.apply()
        renderer.projectionMatrix = camera.combined

        val oldColor = renderer.color.cpy()

        renderer.use {
            renderer.color = Color.RED
            renderer.rect(menuHitboxes.x, menuHitboxes.y, menuHitboxes.width, menuHitboxes.height)
            renderer.rect(retryHitboxes.x, retryHitboxes.y, retryHitboxes.width, retryHitboxes.height)
            renderer.color = oldColor
        }
    }

    private fun renderMenu() {
        viewport.apply()
        batch.projectionMatrix = camera.combined

        val mouseVector = Vector3().set(camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)))

        batch.use {
            // Draw background
            batch.draw(background, 0f, 0f, GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT)
            // Draw logo
            layout.setText(haloFont, "SpaceAgo")
            haloFont.draw(batch, layout, 20f, GameConfig.HUD_HEIGHT - layout.height)

            // Draw game over text.
            batch.draw(gameOver, 330f, 550f, 350f, 100f)

            // Draw score text
            layout.setText(scoreFont, "Score: $score")
            scoreFont.draw(batch, layout, (GameConfig.HUD_WIDTH - layout.width) / 2f, 500f)

            // Draw highscore text
            layout.setText(scoreFont, "Highscore: $highscore")
            scoreFont.draw(batch, layout, (GameConfig.HUD_WIDTH - layout.width) / 2f, 420f)

            // Draw money earned on screen
            layout.setText(scoreFont, "Earned: ${score / 100} scrap")
            scoreFont.draw(batch, layout, (GameConfig.HUD_WIDTH - layout.width) / 2f, 350f)


            val oldColor = scoreFont.color.cpy()
            // Draw buttons text on screen.
            if (inRectangle(menuHitboxes, mouseVector.x, mouseVector.y)) {
                scoreFont.color = Color.YELLOW
                if (Gdx.input.justTouched()) {
                    changeToMenu = true
                }
            }
            layout.setText(scoreFont, "Main Menu")
            scoreFont.draw(batch, layout, (GameConfig.HUD_WIDTH - layout.width) / 2f - 200f, 215f)

            if (inRectangle(retryHitboxes, mouseVector.x, mouseVector.y)) {
                scoreFont.color = Color.YELLOW
                if (Gdx.input.justTouched()) {
                    retry = true
                }
            } else scoreFont.color = oldColor
            layout.setText(scoreFont, "Retry")
            scoreFont.draw(batch, layout, (GameConfig.HUD_WIDTH - layout.width) / 2f + 200f, 215f)

            scoreFont.color = oldColor
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        scoreFont.dispose()
        haloFont.dispose()
    }

    override fun hide() {
        dispose()
    }

    override fun pause() {}
    override fun resume() {}
}
