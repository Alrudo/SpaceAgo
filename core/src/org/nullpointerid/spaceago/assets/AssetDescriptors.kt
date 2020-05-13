package org.nullpointerid.spaceago.assets

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import org.nullpointerid.spaceago.utils.assetDescriptor

object AssetDescriptors {

    val SCORE_FONT = assetDescriptor<BitmapFont>(AssetPaths.SCORE_FONT)
    val MAIN_MENU_ATLAS = assetDescriptor<TextureAtlas>(AssetPaths.MAIN_MENU_ATLAS)
    val GAME_PLAY_ATLAS = assetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY_ATLAS)
}