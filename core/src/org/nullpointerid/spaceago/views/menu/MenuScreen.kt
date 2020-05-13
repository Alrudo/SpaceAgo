package org.nullpointerid.spaceago.views.menu

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
import org.nullpointerid.spaceago.views.game.GameScreen
import org.nullpointerid.spaceago.views.multiplayer.MultiplayerScreen
import org.nullpointerid.spaceago.views.settings.SettingsScreen
import org.nullpointerid.spaceago.views.upgrade.UpgradeShopScreen


class MenuScreen(private val game: SpaceShooter) : Screen {

    private var batch: SpriteBatch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val menuStage: Stage = Stage()

    private val start: Float = 550f
    private val step: Float = 30f
    private val spaceAgo: Label
    private val singleplayerBtn: TextButton
    private val multiplayerBtn: TextButton
    private val upgradeMenuBtn: TextButton
    private val settingsBtn: TextButton
    private val exitBtn: TextButton

    init {
        Gdx.input.inputProcessor = this.menuStage

        spaceAgo = Label("SpaceAgo", game.COMMON_SKIN, "game-title").apply {
            setPosition(20f, menuStage.height - height - 27f)
        }.bind(menuStage)

        singleplayerBtn = TextButton("Singleplayer", game.COMMON_SKIN).extend(20f, 10f).apply {
            setPosition(menuStage.width / 2 - width / 2, start)
        }.bind(menuStage).onClick {
            game.screen = GameScreen(game)
        }

        multiplayerBtn = TextButton("Multiplayer", game.COMMON_SKIN).extend(20f, 10f).apply {
            setPosition(menuStage.width / 2 - width / 2, singleplayerBtn.y - singleplayerBtn.height - step)
        }.bind(menuStage).onClick {
            game.screen = MultiplayerScreen()
        }

        upgradeMenuBtn = TextButton("Upgrades", game.COMMON_SKIN).extend(20f, 10f).apply {
            setPosition(menuStage.width / 2 - width / 2, multiplayerBtn.y - multiplayerBtn.height - step)
        }.bind(menuStage).onClick {
            game.screen = UpgradeShopScreen(game)
        }

        settingsBtn = TextButton("Settings", game.COMMON_SKIN).extend(20f, 10f).apply {
            setPosition(menuStage.width / 2 - width / 2, upgradeMenuBtn.y - upgradeMenuBtn.height - step)
        }.bind(menuStage).onClick {
            game.screen = SettingsScreen(game)
        }

        exitBtn = TextButton("Exit", game.COMMON_SKIN).extend(20f, 10f).apply {
            setPosition(menuStage.width / 2 - width / 2, settingsBtn.y - settingsBtn.height - step)
        }.bind(menuStage).onClick {
            Gdx.app.exit()
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