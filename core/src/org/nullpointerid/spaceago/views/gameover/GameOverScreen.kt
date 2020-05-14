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
import org.nullpointerid.spaceago.SpaceShooter.COMMON_SKIN
import org.nullpointerid.spaceago.SpaceShooter.MBACKGROUND
import org.nullpointerid.spaceago.SpaceShooter.STORAGE
import org.nullpointerid.spaceago.SpaceShooter.assetManager
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.RegionNames
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.utils.gdx.*
import org.nullpointerid.spaceago.views.game.GameScreen
import org.nullpointerid.spaceago.views.menu.MenuScreen

class GameOverScreen(private val score: Int) : Screen {

    companion object {
        @JvmStatic
        private val log = logger<GameOverScreen>()

        private const val STEP = 100f
    }

    private val highscore = STORAGE.getInteger("highscore", 0)
    private val currentCash = STORAGE.getInteger("money", 0)
    private val background = assetManager[AssetDescriptors.GAME_PLAY_ATLAS][RegionNames.GAMEPLAY_BACKGROUND]

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

    init {
        Gdx.input.inputProcessor = menuStage

        spaceAgo = Label("SpaceAgo", COMMON_SKIN, "game-title").apply {
            setPosition(20f, menuStage.height - height - 27f)
        }.bind(menuStage)

        gameOver = Label("Game Over", COMMON_SKIN, "label-h1").apply {
            setPosition(menuStage.width / 2f - width / 2f, 550f)
        }.bind(menuStage)

        currentScore = Label("Score: $score", COMMON_SKIN).apply {
            setPosition(menuStage.width / 2f - width / 2f, gameOver.y - STEP)
        }.bind(menuStage)

        highScore = Label("Highscore: $highscore", COMMON_SKIN).apply {
            setPosition(menuStage.width / 2f - width / 2f, currentScore.y - STEP)
        }.bind(menuStage)

        earnedCash = Label("Earned: ${score / 100} scrap", COMMON_SKIN).apply {
            setPosition(menuStage.width / 2f - width / 2f, highScore.y - STEP)
        }.bind(menuStage)

        toMenuBtn = TextButton("Menu", COMMON_SKIN, "fancy-hover-h3").extend(25f, 10f).apply {
            setPosition(menuStage.width / 2f - width / 2f - 200f, 100f)
        }.bind(menuStage).onClick {
            SpaceShooter.screen = MenuScreen()
        }

        retry = TextButton("Retry", COMMON_SKIN, "fancy-hover-h3").extend(20f, 10f).apply {
            setPosition(menuStage.width / 2f - width / 2f + 200f, 100f)
        }.bind(menuStage).onClick {
            SpaceShooter.screen = GameScreen()
        }
    }

    override fun show() {
        if (score > highscore) {
            STORAGE.putInteger("highscore", score)
        }
        STORAGE.putInteger("money", currentCash + score / 100)
        STORAGE.flush()
        log.debug("${STORAGE.getInteger("money")}")
    }

    override fun render(delta: Float) {

        clearScreen(255, 255, 255, 255)

        batch.use {
            batch.draw(background, 0f, 0f, GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT)
            MBACKGROUND.updateRender(delta, batch)
        }

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                menuStage.actors.forEach {
                    renderer.rect(it)
                }
            }
        }

        menuStage.act(delta)
        menuStage.draw()
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
