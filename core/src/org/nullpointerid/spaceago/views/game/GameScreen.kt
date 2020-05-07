package org.nullpointerid.spaceago.views.game

import com.badlogic.gdx.Screen
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.views.gameover.GameOverScreen
import org.nullpointerid.spaceago.views.multiplayer.MultiplayerController

class GameScreen(val mpController: MultiplayerController? = null) : Screen {

    private lateinit var controller: GameController
    private lateinit var renderer: GameRenderer

    override fun show() {
        controller = GameController(mpController)
        renderer = GameRenderer(SpaceShooter.assetManager, controller)
    }

    override fun render(delta: Float) {
        controller.update(delta)
        renderer.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        renderer.resize(width, height)
    }

    override fun dispose() {
        renderer.dispose()
    }

    override fun hide() {
        dispose()
    }

    override fun pause() {}
    override fun resume() {}
}