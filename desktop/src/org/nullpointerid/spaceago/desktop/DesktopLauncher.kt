package org.nullpointerid.spaceago.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.config.GameConfig

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = GameConfig.WIDTH
        config.height = GameConfig.HEIGHT
        config.title = GameConfig.TITLE
        config.resizable = false
        LwjglApplication(SpaceShooter, config)
    }
}
