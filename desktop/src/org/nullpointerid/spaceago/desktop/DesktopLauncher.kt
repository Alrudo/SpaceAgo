package org.nullpointerid.spaceago.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import org.nullpointerid.spaceago.SpaceShooter

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = SpaceShooter.WIDTH
        config.height = SpaceShooter.HEIGHT
        config.title = SpaceShooter.TITLE
        config.resizable = false
        LwjglApplication(SpaceShooter(), config)
    }
}
