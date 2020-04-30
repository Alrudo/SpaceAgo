package org.nullpointerid.spaceago.screen.menu.settings

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.screen.menu.MenuScreen
import org.nullpointerid.spaceago.utils.*


class SettingsScreen(private val game: SpaceShooter) : Screen {

    private var batch: SpriteBatch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val settingsStage: Stage = Stage()

    private val settings: Label
    private val start: Float = 450f
    private val step: Float = 30f
    private val controlsBtn: TextButton
    private val audioBtn: TextButton
    private val menuBtn: TextButton


    init {
        Gdx.input.inputProcessor = this.settingsStage


        settings = Label("Settings:", game.skin).apply {
            setPosition(settingsStage.width / 2 - width / 2, settingsStage.height - height - 50f)
        }.bind(settingsStage)


        controlsBtn = TextButton("Controls", game.skin)
                .extend(20f, 10f)
                .apply {
                    setPosition(settingsStage.width / 2 - width / 2, start)
                }
                .bind(settingsStage)
                .onClick {
                    game.screen = ControlsScreen(game)
                }

        audioBtn = TextButton("Audio", game.skin)
                .extend(20f, 10f)
                .apply {
                    setPosition(settingsStage.width / 2 - width / 2, controlsBtn.y - controlsBtn.height - step)
                }
                .bind(settingsStage)
                .onClick {
                    game.screen = AudioScreen(game)
                }

        menuBtn = TextButton("Menu", game.skin)
                .extend(20f, 10f)
                .apply {
                    setPosition(settingsStage.width / 2 - width / 2, audioBtn.y - audioBtn.height - step * 5)
                }
                .bind(settingsStage)
                .onClick {
                    game.screen = MenuScreen(game)
                }
    }


    override fun render(delta: Float) {

        clearScreen()

        batch.use {
            batch.draw(game.background, 0f, 0f)
            game.movingBackground.updateRender(delta, batch)
        }

        settingsStage.act(delta)
        settingsStage.draw()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                settingsStage.actors.forEach {
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