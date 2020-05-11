package org.nullpointerid.spaceago.views.gameover

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.views.game.GameScreen
import org.nullpointerid.spaceago.views.menu.MenuScreen

class GameOverScreen(assetManager: AssetManager,
                     private val game: SpaceShooter,
                     private val score: Int) : Screen {

    companion object {
        @JvmStatic
        private val log = logger<GameOverScreen>()

        private const val STEP = 100f
    }

    private val prefs = Gdx.app.getPreferences("spaceshooter")
    private val highscore = prefs.getInteger("highscore", 0)
    private val currentCash = prefs.getInteger("money", 0)

    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val menuStage: Stage = Stage()

    private val spaceAgo: Label
    private val gameOver: Label
    private val currentScore: Label
    private val highScore: Label
    private val earnedCash: Label
    private val toMenuBtn: TextButton
    private val retry: TextButton

    private val gameAtlas = assetManager[AssetDescriptors.GAME_PLAY_ATLAS]
    private val background = gameAtlas[RegionNames.GAMEPLAY_BACKGROUND]

    init {
        Gdx.input.inputProcessor = menuStage

        spaceAgo = Label("SpaceAgo", game.COMMON_SKIN, "game-title").apply {
            setPosition(20f, menuStage.height - height - 27f)
        }.bind(menuStage)

        gameOver = Label("Game Over", game.COMMON_SKIN, "label-h1").apply {
            setPosition(menuStage.width / 2f - width / 2f, 550f)
        }.bind(menuStage)

        currentScore = Label("Score: $score", game.COMMON_SKIN).apply {
            setPosition(menuStage.width / 2f - width / 2f, gameOver.y - STEP)
        }.bind(menuStage)

        highScore = Label("Highscore: $highscore", game.COMMON_SKIN).apply {
            setPosition(menuStage.width / 2f - width / 2f, currentScore.y - STEP)
        }.bind(menuStage)

        earnedCash = Label("Earned: ${score / 100} scrap", game.COMMON_SKIN).apply {
            setPosition(menuStage.width / 2f - width / 2f, highScore.y - STEP)
        }.bind(menuStage)

        toMenuBtn = TextButton("Menu", game.COMMON_SKIN, "fancy-hover-h3").extend(20f, 10f).apply {
            setPosition(menuStage.width / 2f - width / 2f - 200f, 100f)
        }.bind(menuStage).onClick {
            game.screen = MenuScreen(game)
        }

        retry = TextButton("Retry", game.COMMON_SKIN, "fancy-hover-h3").extend(20f, 10f).apply {
            setPosition(menuStage.width / 2f - width / 2f + 200f, 100f)
        }.bind(menuStage).onClick {
            game.screen = GameScreen(game)
        }
    }

    override fun show() {
        if (score > highscore) {
            prefs.putInteger("highscore", score)
        }
        prefs.putInteger("money", currentCash + score / 100)
        prefs.flush()
        log.debug("${prefs.getInteger("money")}")
    }

    override fun render(delta: Float) {

        clearScreen(255, 255, 255, 255)

        batch.use {
            batch.draw(background, 0f, 0f, GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT)
            game.movingBackground.updateRender(delta, batch)
        }

        menuStage.act(delta)
        menuStage.draw()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                menuStage.actors.forEach {
                    renderer.rect(it)
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
    }

    override fun hide() {
        dispose()
    }

    override fun pause() {}
    override fun resume() {}
}
