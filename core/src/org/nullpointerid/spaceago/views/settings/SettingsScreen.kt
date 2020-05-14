package org.nullpointerid.spaceago.views.settings

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
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
import org.nullpointerid.spaceago.SpaceShooter.COMMON_SKIN
import org.nullpointerid.spaceago.SpaceShooter.SETTINGS_SKIN
import org.nullpointerid.spaceago.SpaceShooter.STORAGE
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.utils.gdx.*
import org.nullpointerid.spaceago.views.menu.MenuScreen
import kotlin.math.roundToInt

class SettingsScreen() : Screen {

    companion object {
        @JvmStatic
        private val log = logger<SettingsScreen>()

        private const val STEP_Y = 20f
        private const val STEP_X = 50f
        private const val DESCRIPTION_X = 300f
        private const val CONTENT_WIDTH = 400f

        private const val BUTTON_INPUT_MESSAGE = "Press not used key"
    }

    private val buttons = HashMap<TextButton, String>()

    private val moveUpValue = STORAGE.getString("moveUp", "W")
    private val moveDownValue = STORAGE.getString("moveDown", "S")
    private val moveLeftValue = STORAGE.getString("moveLeft", "A")
    private val moveRightValue = STORAGE.getString("moveRight", "D")
    private val shootValue = STORAGE.getString("shoot", "Space")
    private val ultimateWeaponValue = STORAGE.getString("ultimate", "N")

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
    private val moveUpKey: TextButton
    private val moveDownKey: TextButton
    private val moveLeftKey: TextButton
    private val moveRightKey: TextButton
    private val shootKey: TextButton
    private val specialKey: TextButton

    private val slider: Slider

    init {
        log.debug(STORAGE.getString("moveUp"))
        input.inputProcessor = settingsStage

        spaceAgo = Label("SpaceAgo", COMMON_SKIN, "game-title").apply {
            setPosition(20f, settingsStage.height - height - 27f)
        }.bind(settingsStage)

        moveUp = Label("Move Up:", COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, 630f)
        }.bind(settingsStage)

        moveUpKey = TextButton(moveUpValue, COMMON_SKIN, "fancy-hover-h5").apply {
            buttons[this] = "moveUp"
            setBounds(moveUp.x + moveUp.width + STEP_X, moveUp.y, CONTENT_WIDTH, moveUp.height)
        }.bind(settingsStage)

        moveUpKey.onClick {
            bindChanger(moveUpKey)
        }

        moveDown = Label("Move Down:", COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, moveUp.y - height - STEP_Y)
        }.bind(settingsStage)

        moveDownKey = TextButton(moveDownValue, COMMON_SKIN, "fancy-hover-h5").apply {
            buttons[this] = "moveDown"
            setBounds(moveDown.x + moveDown.width + STEP_X, moveDown.y, CONTENT_WIDTH, moveDown.height)
        }.bind(settingsStage)

        moveDownKey.onClick {
            bindChanger(moveDownKey)
        }

        moveLeft = Label("Move Left:", COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, moveDown.y - height - STEP_Y)
        }.bind(settingsStage)

        moveLeftKey = TextButton(moveLeftValue, COMMON_SKIN, "fancy-hover-h5").apply {
            buttons[this] = "moveLeft"
            setBounds(moveLeft.x + moveLeft.width + STEP_X, moveLeft.y, CONTENT_WIDTH, moveLeft.height)
        }.bind(settingsStage)

        moveLeftKey.onClick {
            bindChanger(moveLeftKey)
        }

        moveRight = Label("Move Right:", COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, moveLeft.y - height - STEP_Y)
        }.bind(settingsStage)

        moveRightKey = TextButton(moveRightValue, COMMON_SKIN, "fancy-hover-h5").apply {
            buttons[this] = "moveRight"
            setBounds(moveRight.x + moveRight.width + STEP_X, moveRight.y, CONTENT_WIDTH, moveRight.height)
        }.bind(settingsStage)

        moveRightKey.onClick {
            bindChanger(moveRightKey)
        }

        shoot = Label("Shoot:", COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, moveRight.y - height - STEP_Y)
        }.bind(settingsStage)

        shootKey = TextButton(shootValue, COMMON_SKIN, "fancy-hover-h5").apply {
            buttons[this] = "shoot"
            setBounds(shoot.x + shoot.width + STEP_X, shoot.y, CONTENT_WIDTH, shoot.height)
        }.bind(settingsStage)

        shootKey.onClick {
            bindChanger(shootKey)
        }

        specialWeapon = Label("Special Weapon:", COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, shoot.y - height - STEP_Y)
        }.bind(settingsStage)

        specialKey = TextButton(ultimateWeaponValue, COMMON_SKIN, "fancy-hover-h5").apply {
            buttons[this] = "ultimate"
            setBounds(specialWeapon.x + specialWeapon.width + STEP_X, specialWeapon.y, CONTENT_WIDTH, specialWeapon.height)
        }.bind(settingsStage)

        specialKey.onClick {
            bindChanger(specialKey)
        }

        volume = Label("Volume:", COMMON_SKIN, "label-h5").apply {
            setPosition(DESCRIPTION_X - width, specialWeapon.y - height - STEP_Y)
        }.bind(settingsStage)

        slider = Slider(0f, 1f, 0.01f, false, SETTINGS_SKIN).apply {
            setBounds(volume.x + volume.width + STEP_X, volume.y, CONTENT_WIDTH, volume.height)
            value = STORAGE.getFloat("volume", 0.5f)
        }.bind(settingsStage)

        volumeValue = Label("${(slider.value * 100).toInt()}%", COMMON_SKIN, "label-h5").apply {
            setPosition(slider.x + slider.width + STEP_X, slider.y)
        }.bind(settingsStage)

        slider.onDrag {
            log.debug(slider.value.toString())
            volumeValue.setText("${((slider.value * 100).roundToInt())}%")
        }.onDragStop {
            STORAGE.putFloat("volume", slider.value)
            Audio.volume = slider.value
        }

        toMenu = TextButton("Menu", COMMON_SKIN, "fancy-hover-h3").extend(20f, 10f).apply {
            setPosition(settingsStage.width / 2f - width / 2f, 50f)
        }.bind(settingsStage).onClick {
            SpaceShooter.screen = MenuScreen()
        }
    }


    override fun render(delta: Float) {

        clearScreen()

        batch.use {
            batch.draw(SpaceShooter.BACKGROUND, 0f, 0f)
            SpaceShooter.MBACKGROUND.updateRender(delta, batch)
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

    private fun bindChanger(button: TextButton) {
        val prevKey = button.text.toString()
        button.setText(BUTTON_INPUT_MESSAGE)

        //Gets user input for pressed key
        input.inputProcessor = object : InputAdapter() {
            override fun keyUp(keycode: Int): Boolean {
                val key = Input.Keys.toString(keycode)
                for (btn in buttons.keys) {
                    if (btn.text.toString() == key) {
                        button.setText(prevKey)
                        input.inputProcessor = settingsStage
                        return true
                    }
                }
                button.setText(key)
                STORAGE.putString(buttons[button], key)
                STORAGE.flush()
                input.inputProcessor = settingsStage
                return true
            }
        }
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}

}