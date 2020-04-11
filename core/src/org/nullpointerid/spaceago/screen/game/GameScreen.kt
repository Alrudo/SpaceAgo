package org.nullpointerid.spaceago.screen.game

import com.badlogic.gdx.Screen
import org.nullpointerid.spaceago.SpaceShooter

class GameScreen(game: SpaceShooter) : Screen {

    private val assetManager = game.assetManager
    private lateinit var controller: GameController
    private lateinit var renderer: GameRenderer

    override fun show() {
        controller = GameController()
        renderer = GameRenderer(assetManager, controller)
    }

    override fun render(delta: Float) {
        controller.update(delta)
        renderer.render()
    }

    override fun resize(width: Int, height: Int) {
        renderer.resize(width, height)
    }

    override fun dispose() {
        renderer.dispose()
    }

    override fun hide() {
        // WARNING : screens are not disposed automatically!
        // without dispose() call screen will be not disposed.
        dispose()
    }

    override fun pause() {}
    override fun resume() {}
}