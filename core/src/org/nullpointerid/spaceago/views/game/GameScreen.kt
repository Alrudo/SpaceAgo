package org.nullpointerid.spaceago.views.game

import com.badlogic.gdx.Screen
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.utils.maintainFPS
import org.nullpointerid.spaceago.views.multiplayer.MultiplayerController
import java.lang.Exception
import java.util.*

class GameScreen(val mpController: MultiplayerController? = null) : Screen {

    lateinit var controller: GameController
    lateinit var renderer: GameRenderer

    override fun show() {
        controller = GameController(mpController)
        renderer = GameRenderer(controller.world)
    }

    override fun render(delta: Float) {
        maintainFPS(60)
        if (mpController == null) {
            controller.update(delta)
            renderer.render(delta)
            return
        }

        if(mpController.role == MultiplayerController.Role.SERVER){
            controller.update(delta)
            mpController.serverConnection!!.sendTCP(controller.world)
        }else{
            renderer.world = controller.world
            controller.updateClient(delta)
        }
        renderer.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        renderer.resize(width, height)
    }

    override fun dispose() {
        renderer.dispose()
    }

    override fun hide() {
        dispose()
    }

    override fun pause() {}
    override fun resume() {}
}