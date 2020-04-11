package org.nullpointerid.spaceago.screen.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import org.nullpointerid.spaceago.config.GameConfig
import org.nullpointerid.spaceago.utils.logger

class GameRenderer(assetManager: AssetManager,
                   private val controller: GameController) : Disposable {

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.)

    fun render() {

    }

    fun resize(with: Int, height: Int) {

    }

    override fun dispose() {

    }
}