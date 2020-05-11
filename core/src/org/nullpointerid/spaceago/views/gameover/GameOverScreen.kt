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
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.views.game.GameScreen
import org.nullpointerid.spaceago.views.menu.MenuScreen
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.views.game.GameController
import org.nullpointerid.spaceago.views.settings.SettingsScreen

class GameOverScreen(assetManager: AssetManager,
                     private val game: SpaceShooter,
                     private val score: Int) : Screen {

    companion object {
        @JvmStatic
        private val log = logger<GameOverScreen>()
    }

    var gc = GameController
    private var changeScreen = false
    private val gameOverStage: Stage = Stage()

    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val layout = GlyphLayout()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val prefs = Gdx.app.getPreferences("spaceshooter")
    private val highscore = prefs.getInteger("highscore", 0)
    private val currentCash = prefs.getInteger("money", 0)

    private val start: Float = 550f
    private val step: Float = 30f
    private val gameOverLbl: Label
    private val scoreLbl: Label
    private val highScoreLbl : Label
    private val currentCashLbl : Label
    private val exitBtn: TextButton
    private val retryBtn: TextButton
    private val mainMenuBtn: TextButton


    init {
        Gdx.input.inputProcessor = this.gameOverStage

        gameOverLbl = Label("Game Over:", game.COMMON_SKIN, "game-title").apply {
            setPosition(gameOverStage.width / 2 - width / 2, gameOverStage.height - height - 50f)
        }.bind(gameOverStage)

        scoreLbl = Label("Score: $score", game.COMMON_SKIN).apply {
            setPosition(gameOverStage.width / 2 - width / 2, gameOverStage.height - height - 200f)
        }.bind(gameOverStage)

        highScoreLbl = Label("Highscore: $highscore", game.COMMON_SKIN).apply {
            setPosition(gameOverStage.width / 2 - width / 2, gameOverStage.height - height - 300f)
        }.bind(gameOverStage)

        currentCashLbl = Label("Earned: ${score / 100} scrap", game.COMMON_SKIN).apply {
            setPosition(gameOverStage.width / 2 - width / 2, gameOverStage.height - height - 400f)
        }.bind(gameOverStage)

        exitBtn = TextButton("Exit", game.COMMON_SKIN)
                .extend(20f, 10f)
                .apply {
                    setPosition(gameOverStage.width / 2 - width / 2, 50f)
                }
                .bind(gameOverStage)
                .onClick {
                    Gdx.app.exit()
                }

        retryBtn = TextButton("Retry", game.COMMON_SKIN)
                .extend(20f, 10f)
                .apply {
                    setPosition(gameOverStage.width / 2 - width / 2 + 130f, 170f)
                }
                .bind(gameOverStage)
                .onClick {
                    game.screen = GameScreen(game)
                }

        mainMenuBtn = TextButton("Main Menu", game.COMMON_SKIN)
                .extend(20f, 10f)
                .apply {
                    setPosition(gameOverStage.width / 2 - 270f, 170f)
                }
                .bind(gameOverStage)
                .onClick {
                    game.screen = MenuScreen(game)
                }

        if (score > highscore) {
            prefs.putInteger("highscore", score)
        }
        prefs.putInteger("money", currentCash + score / 100)
        prefs.flush()
        log.debug("${prefs.getInteger("money")}")
    }

    override fun render(delta: Float) {

        clearScreen()

        batch.use {
            batch.draw(game.background, 0f, 0f)
            game.movingBackground.updateRender(delta, batch)
        }

        gameOverStage.act(delta)
        gameOverStage.draw()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                gameOverStage.actors.forEach {
                    renderer.rect(it)
                }
            }
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

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
}
