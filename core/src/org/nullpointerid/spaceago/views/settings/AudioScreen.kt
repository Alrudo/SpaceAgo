package org.nullpointerid.spaceago.views.settings

import com.badlogic.gdx.Gdx.input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.views.game.GameController
import org.nullpointerid.spaceago.utils.*


class AudioScreen(private val game: SpaceShooter) : Screen {

    var gc = GameController
    private var changeScreen = false

    private val audioStage: Stage = Stage()

    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val layout = GlyphLayout()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val start: Float = 550f
    private val step: Float = 30f
    private val audio: Label
    private val backBtn: TextButton

    init {
        input.inputProcessor = this.audioStage

        audio = Label("Audio:", game.COMMON_SKIN).apply {
            setPosition(audioStage.width / 2 - width / 2, audioStage.height - height - 50f)
        }.bind(audioStage)

        backBtn = TextButton("Back", game.COMMON_SKIN)
                .extend(20f, 10f)
                .apply {
                    setPosition(audioStage.width / 2 - width / 2, 50f)
                }
                .bind(audioStage)
                .onClick {
                    game.screen = SettingsScreen(game)
                }
    }
    override fun render(delta: Float) {

        clearScreen()

        batch.use {
            batch.draw(game.background, 0f, 0f)
            game.movingBackground.updateRender(delta, batch)
        }

        audioStage.act(delta)
        audioStage.draw()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                audioStage.actors.forEach {
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