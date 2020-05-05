package org.nullpointerid.spaceago.views.multiplayer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.SpaceShooter.COMMON_SKIN
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.*
import org.nullpointerid.spaceago.views.menu.MenuScreen

class MultiplayerScreen : Screen {

    private val CNTLR = MultiplayerController
    private val batch = SpriteBatch()
    private val renderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera)

    private val menuStage: Stage = Stage()

    private val spaceAgo: Label
    val serverDiv: Table
    val clientDiv: Table
    val waitingDiv: Table
    val connectingDiv: Table

    private val portInput: TextField
    private val portClientInput: TextField
    private val openSocketBtn: TextButton
    private val exitBtn: TextButton

    init {
        Gdx.input.inputProcessor = this.menuStage

        spaceAgo = Label("SpaceAgo", COMMON_SKIN, "game-title").apply {
            setPosition(20f, menuStage.height - height - 27f)
        }.bind(menuStage)

        serverDiv = Table().apply { defaults().left().pad(5f, 0f, 5f, 0f); setBounds(100f, 330f, 325f, 270f) }.bind(menuStage)
        clientDiv = Table().apply { defaults().left().pad(5f, 0f, 5f, 0f); setBounds(600f, 330f, 300f, 270f) }.bind(menuStage)
        waitingDiv = Table().apply { defaults().left().pad(5f, 0f, 5f, 0f); setBounds(300f, 330f, 400f, 270f); isVisible = false }.bind(menuStage)
        connectingDiv = Table().apply { defaults().left().pad(5f, 0f, 5f, 0f); setBounds(300f, 330f, 400f, 270f); isVisible = false }.bind(menuStage)

        // ### START HOST SECTION ###
        Label("Become game host:", COMMON_SKIN).apply { serverDiv.add(this).colspan(2).row() }

        Label("Port: ", COMMON_SKIN).apply { serverDiv.add(this) }

        portInput = TextField("4569", COMMON_SKIN).apply { maxLength = 5; serverDiv.add(this).left().row() }.apply {
            setTextFieldFilter { textField, c ->
                if (Character.isDigit(c) && (textField.text + c).toInt() > 65535) {
                    text = "65535"
                    cursorPosition = 5
                    return@setTextFieldFilter true
                }
                Character.isDigit(c)
            }
        }

        openSocketBtn = TextButton("Host game", COMMON_SKIN, "fancy-h3").extend(20f).apply { serverDiv.add(this).colspan(2) }.onClick {
            MultiplayerController.startServer(portInput.text.toInt()-1, portInput.text.toInt())
        }
        // ### END HOST SECTION ###

        // ### START CLIENT SECTION ###

        Label("Join your friend:", COMMON_SKIN).apply { clientDiv.add(this).colspan(2).row() }

        Label("Port: ", COMMON_SKIN).apply { clientDiv.add(this) }

        portClientInput = TextField("4569", COMMON_SKIN).apply { maxLength = 5; clientDiv.add(this).left().row() }.apply {
            setTextFieldFilter { textField, c ->
                if (Character.isDigit(c) && (textField.text + c).toInt() > 65535) {
                    text = "65535"
                    cursorPosition = 5
                    return@setTextFieldFilter true
                }
                Character.isDigit(c)
            }
        }

        TextButton("Search", COMMON_SKIN, "fancy-h3").apply { setPosition(600f, 460f); clientDiv.add(this).colspan(2).row() }.onClick {
            MultiplayerController.startClient(portInput.text.toInt()-1, portInput.text.toInt())
        }
        // ### END CLIENT SECTION ###

        // ### START HOST SECTION ###
        Label("Waiting for client", COMMON_SKIN).apply {
            waitingDiv.add(this).colspan(2).row()
        }

        Label("Port: ", COMMON_SKIN).apply { waitingDiv.add(this) }
        // ### END CLIENT SECTION ###


        exitBtn = TextButton("Menu", COMMON_SKIN, "fancy-h1").apply {
            setPosition(menuStage.width / 2 - width / 2, 100f)
        }.bind(menuStage).onClick {
            MultiplayerController.closeServer()
            SpaceShooter.screen = MenuScreen(SpaceShooter)
        }

        MultiplayerController.screen = this
    }

    override fun render(delta: Float) {
        clearScreen()

        batch.use {
            batch.draw(SpaceShooter.background, 0f, 0f)
            SpaceShooter.movingBackground.updateRender(delta, batch)
        }

        menuStage.act(delta)
        menuStage.draw()

        if (GameConfig.DEBUG_MODE) {
            viewport.drawGrid(renderer, 100f)
            renderer.use {
                menuStage.actors.filter { it.isVisible }.forEach {
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