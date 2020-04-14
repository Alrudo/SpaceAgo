package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport

@JvmOverloads
fun Viewport.drawGrid(renderer: ShapeRenderer, cellSize: Float = 1f) {
    val oldColor = renderer.color.cpy() // copy the previous color.
    val doubleWorldWidth = worldWidth * 2
    val doubleWorldHeight = worldHeight * 2

    apply()
    renderer.use {
        renderer.color = Color.WHITE

        // draw vertical lines
        var x = -doubleWorldWidth
        while (x < doubleWorldWidth) {
            renderer.line(x, -doubleWorldHeight, x, doubleWorldHeight)
            x += cellSize
        }

        // draw horizontal lines
        var y = -doubleWorldHeight
        while (y < doubleWorldHeight) {
            renderer.line(-doubleWorldHeight, y, doubleWorldHeight, y)
            y += cellSize
        }

        // make 0/0 lines recognizable.
        renderer.color = Color.RED
        renderer.line(0f, -doubleWorldHeight, 0f, doubleWorldHeight)
        renderer.line(-doubleWorldWidth, 0f, doubleWorldWidth, 0f)

        // make world bounds recognizable.
        renderer.color = Color.GREEN
        renderer.line(0f, worldHeight, worldWidth, worldHeight)
        renderer.line(worldWidth, 0f, worldWidth, worldHeight)
    }

    renderer.color = oldColor
}
