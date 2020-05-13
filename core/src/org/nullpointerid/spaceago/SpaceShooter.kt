package org.nullpointerid.spaceago

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Logger
import org.nullpointerid.spaceago.assets.AssetDescriptors
import org.nullpointerid.spaceago.assets.AssetPaths
import org.nullpointerid.spaceago.views.loading.LoadingScreen
import org.nullpointerid.spaceago.tools.MovingBackground
import org.nullpointerid.spaceago.utils.Fonts
import org.nullpointerid.spaceago.utils.toInternalFile

object SpaceShooter : Game() {

    val assetManager = AssetManager()
    lateinit var gameAtlas: TextureAtlas
    lateinit var background: TextureRegion
    lateinit var movingBackground: MovingBackground
    lateinit var explosionTexture: Texture

    val COMMON_SKIN = Skin()

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        assetManager.logger.level = Logger.DEBUG
        movingBackground = MovingBackground()
        explosionTexture = Texture(AssetPaths.EXPLOSION.toInternalFile())
        setScreen(LoadingScreen(this))
    }

    override fun resize(width: Int, height: Int) {
        movingBackground.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
        gameAtlas.dispose()
        assetManager.dispose()
        explosionTexture.dispose()
        Fonts.dispose()
    }
}