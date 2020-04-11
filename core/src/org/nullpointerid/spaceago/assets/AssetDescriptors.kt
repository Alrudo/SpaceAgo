package org.nullpointerid.spaceago.assets

import com.badlogic.gdx.graphics.g2d.BitmapFont
import org.nullpointerid.spaceago.utils.assetDescriptor

object AssetDescriptors {

    val GAME_OVER_FONT = assetDescriptor<BitmapFont>(AssetPaths.GAME_OVER_FONT)
    val HALO_FONT = assetDescriptor<BitmapFont>(AssetPaths.HALO_FONT)
    val HUNGER_GAMES_FONT = assetDescriptor<BitmapFont>(AssetPaths.HUNGER_GAMES_FONT)
    val SCORE_FONT = assetDescriptor<BitmapFont>(AssetPaths.SCORE_FONT)
}