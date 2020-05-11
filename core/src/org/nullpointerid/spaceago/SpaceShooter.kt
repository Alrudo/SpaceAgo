package org.nullpointerid.spaceago

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Logger
import org.nullpointerid.spaceago.views.loading.LoadingScreen
import org.nullpointerid.spaceago.tools.MovingBackground
import org.nullpointerid.spaceago.utils.Fonts

object SpaceShooter : Game() {

    val assetManager = AssetManager()
    lateinit var background: TextureRegion
    lateinit var movingBackground: MovingBackground

    val COMMON_SKIN = Skin()
    val COMMON_SKIN2= Skin()

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
        Fonts.dispose()
    }
}