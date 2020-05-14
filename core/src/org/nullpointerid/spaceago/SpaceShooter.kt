package org.nullpointerid.spaceago

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Logger
import org.nullpointerid.spaceago.assets.AssetPaths
import org.nullpointerid.spaceago.views.loading.LoadingScreen
import org.nullpointerid.spaceago.tools.MovingBackground
import org.nullpointerid.spaceago.utils.Fonts
import org.nullpointerid.spaceago.utils.gdx.toInternalFile

object SpaceShooter : Game() {

    lateinit var STORAGE: Preferences
    val assetManager = AssetManager()
    lateinit var GAME_ATLAS: TextureAtlas
    lateinit var BACKGROUND: TextureRegion
    lateinit var MBACKGROUND: MovingBackground
    lateinit var explosionTexture: Texture

    val COMMON_SKIN = Skin()
    val SETTINGS_SKIN = Skin()

    override fun create() {
        STORAGE = Gdx.app.getPreferences("spaceshooter")
        Gdx.app.logLevel = Application.LOG_DEBUG
        assetManager.logger.level = Logger.DEBUG
        MBACKGROUND = MovingBackground()
        explosionTexture = Texture(AssetPaths.EXPLOSION.toInternalFile())
        setScreen(LoadingScreen(this))
    }

    override fun resize(width: Int, height: Int) {
        MBACKGROUND.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
        GAME_ATLAS.dispose()
        assetManager.dispose()
        explosionTexture.dispose()
        Fonts.dispose()
    }
}