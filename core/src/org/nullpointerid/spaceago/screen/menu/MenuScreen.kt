package org.nullpointerid.spaceago.screen.menu

import com.badlogic.gdx.Screen
import org.nullpointerid.spaceago.SpaceShooter

class MenuScreen(game: SpaceShooter) : Screen {
    companion object {
        private const val EXIT_BUTTON_WIDTH = 150
        private const val EXIT_BUTTON_HEIGHT = 50
        private const val PLAY_BUTTON_WIDTH = 150
        private const val PLAY_BUTTON_HEIGHT = 50
        private const val EXIT_BUTTON_Y = 250
        private const val PLAY_BUTTON_Y = 325
    }

    override fun show() {

    }

    override fun render(delta: Float) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {

    }

    override fun hide() {
        // WARNING: Screens are not disposed automatically!
        // without dispose() call screen will be not disposed.
        dispose()
    }

}