package org.nullpointerid.spaceago

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.nullpointerid.spaceago.screen.MainMenuScreenOld
import org.nullpointerid.spaceago.tools.MovingBackgroundOld

@Deprecated("Old Version", ReplaceWith("SpaceShooter"), DeprecationLevel.WARNING)
class SpaceShooterOld : Game() {
    companion object {
        const val WIDTH = 600
        const val HEIGHT = 800
        const val TITLE = "SpaceAgo"
    }

    lateinit var batch: SpriteBatch
    lateinit var movingBackgroundOld: MovingBackgroundOld

    override fun create() {
        batch = SpriteBatch()
        movingBackgroundOld = MovingBackgroundOld()
        setScreen(MainMenuScreenOld(this))
    }

    override fun dispose() {}

    override fun resize(width: Int, height: Int) {
        movingBackgroundOld.resize(width, height)
        super.resize(width, height)
    }
}
