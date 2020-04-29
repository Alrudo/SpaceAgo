package org.nullpointerid.spaceago

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Logger
import org.nullpointerid.spaceago.screen.loading.LoadingScreen
import org.nullpointerid.spaceago.tools.MovingBackground

class SpaceShooter : Game() {

    val assetManager = AssetManager()
    lateinit var movingBackground: MovingBackground

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        assetManager.logger.level = Logger.DEBUG
        movingBackground = MovingBackground()
        setScreen(LoadingScreen(this))
    }

    override fun resize(width: Int, height: Int) {
        movingBackground.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
        assetManager.dispose()
    }
}