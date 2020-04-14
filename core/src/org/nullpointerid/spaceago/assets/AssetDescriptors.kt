package org.nullpointerid.spaceago.assets

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import org.nullpointerid.spaceago.utils.assetDescriptor

object AssetDescriptors {

    val GAME_OVER_FONT = assetDescriptor<BitmapFont>(AssetPaths.GAME_OVER_FONT)
    val HALO_FONT = assetDescriptor<BitmapFont>(AssetPaths.HALO_FONT)
    val HUNGER_GAMES_FONT = assetDescriptor<BitmapFont>(AssetPaths.HUNGER_GAMES_FONT)
    val SCORE_FONT = assetDescriptor<BitmapFont>(AssetPaths.SCORE_FONT)
    val MAIN_MENU_ATLAS = assetDescriptor<TextureAtlas>(AssetPaths.MAIN_MENU_ATLAS)
    val GAME_OVER_ATLAS = assetDescriptor<TextureAtlas>(AssetPaths.GAME_OVER_ATLAS)
    val GAME_PLAY_ATLAS = assetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY_ATLAS)
}