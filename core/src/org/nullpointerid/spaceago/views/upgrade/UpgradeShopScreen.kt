package org.nullpointerid.spaceago.views.upgrade

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.views.menu.MenuScreen

class UpgradeShopScreen(private val game: SpaceShooter) : Screen {

    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val menuStage: Stage = Stage()

    private val spaceAgo: Label
    private val exitBtn: TextButton

    init {
        Gdx.input.inputProcessor = this.menuStage

        spaceAgo = Label("SpaceAgo", game.COMMON_SKIN).apply {
            setPosition(20f, menuStage.height - height - 27f)
        }.bind(menuStage)


        exitBtn = TextButton("Menu", game.COMMON_SKIN).extend(20f, 10f).apply {
            setPosition(menuStage.width / 2 - width / 2, 100f)
        }.bind(menuStage).onClick {
            game.screen = MenuScreen(game)
        }
    }

    override fun render(delta: Float) {
        clearScreen(255, 255, 255, 255)

        batch.use {
            batch.draw(game.background, 0f, 0f)
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

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
}