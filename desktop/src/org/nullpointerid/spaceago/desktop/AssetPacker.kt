package org.nullpointerid.spaceago.desktop

import com.badlogic.gdx.tools.texturepacker.TexturePacker

object AssetPacker {

    const val DRAW_DEBUG_OUTLINE = false // removes debug squares on edges of assets.

    const val RAW_ASSETS_PATH = "core/assets/images/raw"
    const val ASSETS_PATH = "core/assets/images/assets"
}


fun main() {
    val settings = TexturePacker.Settings().apply {
        debug = AssetPacker.DRAW_DEBUG_OUTLINE
        maxWidth = 2048  // set max width (default = 1024)
        maxHeight = 2048  // set max height (default = 1024)
    }

    TexturePacker.process(settings,
            "${AssetPacker.RAW_ASSETS_PATH}/main_menu",
            "${AssetPacker.ASSETS_PATH}/main_menu",
            "main_menu")
}
