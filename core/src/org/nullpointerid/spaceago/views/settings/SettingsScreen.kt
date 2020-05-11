package org.nullpointerid.spaceago.views.settings

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.views.game.GameController
import org.nullpointerid.spaceago.views.menu.MenuScreen
import kotlin.math.roundToInt

class SettingsScreen(private val game: SpaceShooter) : Screen {

    companion object {
        @JvmStatic
        private val log = logger<SettingsScreen>()

        private const val STEP_Y = 20f
        private const val STEP_X = 50f
        private const val DESCRIPTION_X = 300f
    }

    private val controller = GameController

    private var batch: SpriteBatch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val settingsStage: Stage = Stage()

    private val spaceAgo: Label
    private val moveUp: Label
    private val moveDown: Label
    private val moveLeft: Label
    private val moveRight: Label
    private val shoot: Label
    private val specialWeapon: Label
    private val volume: Label
    private val volumeValue: Label
    private val toMenu: TextButton
    private val slider: Slider

    init {
        Gdx.input.inputProcessor = settingsStage

        spaceAgo = Label("SpaceAgo", game.COMMON_SKIN, "game-title").apply {
            setPosition(20f, settingsStage.height - height - 27f)
        }.bind(settingsStage)

        moveUp = Label("Move Up:", game.COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, 630f)
        }.bind(settingsStage)

        moveDown = Label("Move Down:", game.COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, moveUp.y - height - STEP_Y)
        }.bind(settingsStage)

        moveLeft = Label("Move Left:", game.COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, moveDown.y - height - STEP_Y)
        }.bind(settingsStage)

        moveRight = Label("Move Right:", game.COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, moveLeft.y - height - STEP_Y)
        }.bind(settingsStage)

        shoot = Label("Shoot:", game.COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, moveRight.y - height - STEP_Y)
        }.bind(settingsStage)

        specialWeapon = Label("Special Weapon:", game.COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, shoot.y - height - STEP_Y)
        }.bind(settingsStage)

        volume = Label("Volume:", game.COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, specialWeapon.y - height - STEP_Y)
        }.bind(settingsStage)

        slider = Slider(0f, 1f, 0.01f, false, game.SETTINGS_SKIN).apply {
            setBounds(volume.x + volume.width + STEP_X, volume.y, 400f, 50f)
            value = controller.volume  // TODO: Change the value storage from controller to preferences.
        }.bind(settingsStage)

        volumeValue = Label("${(slider.value * 100).toInt()}%", game.COMMON_SKIN, "label-h5").apply {
            setPosition(slider.x + slider.width + STEP_X, slider.y)
        }.bind(settingsStage)

        slider.onDrag {
            log.debug(slider.value.toString())
            volumeValue.setText("${((slider.value * 100).roundToInt())}%")
        } // TODO: Add onUp listener which will save the current slider value to preferences.

        toMenu = TextButton("Menu", game.COMMON_SKIN, "fancy-hover-h3").extend(20f, 10f).apply {
            setPosition(settingsStage.width / 2f - width / 2f, 50f)
        }.bind(settingsStage).onClick {
            game.screen = MenuScreen(game)
        }
    }


    override fun render(delta: Float) {

        clearScreen()

        batch.use {
            batch.draw(game.background, 0f, 0f)
            game.movingBackground.updateRender(delta, batch)
        }

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                settingsStage.actors.forEach {
                    renderer.rect(it)
                }
            }
        }

        settingsStage.act(delta)
        settingsStage.draw()
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