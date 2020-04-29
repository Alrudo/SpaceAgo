package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

object Fonts {
    val MENU = BitmapFont("fonts/MenuFont.fnt".toInternalFile())
    val MENU_YELLOW = BitmapFont("fonts/MenuFontYellow.fnt".toInternalFile())
    val HALO = FreeTypeFontGenerator("fonts/Halo3.ttf".toInternalFile()).generateFont(
            FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                size = 75
                borderWidth = 5f
                borderColor = Color.BLACK
                color = Color.GRAY
            })

    fun dispose(){
        MENU.dispose()
        MENU_YELLOW.dispose()
        HALO.dispose()
    }
}