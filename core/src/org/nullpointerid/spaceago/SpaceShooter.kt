package org.nullpointerid.spaceago

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.nullpointerid.spaceago.screens.MainMenuScreen
import org.nullpointerid.spaceago.tools.MovingBackground

class SpaceShooter : Game() {
    companion object {
        const val WIDTH = 600
        const val HEIGHT = 800
        const val TITLE = "SpaceAgo"
    }

    lateinit var batch: SpriteBatch
    lateinit var movingBackground: MovingBackground

    override fun create() {
        batch = SpriteBatch()
        movingBackground = MovingBackground()
        setScreen(MainMenuScreen(this))
    }

    override fun dispose() {}

    override fun resize(width: Int, height: Int) {
        movingBackground.resize(width, height)
        super.resize(width, height)
    }
}