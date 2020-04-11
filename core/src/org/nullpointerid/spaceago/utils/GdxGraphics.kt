package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import java.awt.Color

@JvmOverloads  // if you want to mix Java and Kotlin
fun clearScreen(color: Color = Color.BLACK) = clearScreen(color.red, color.green, color.blue, color.alpha)

fun clearScreen(red: Int, green: Int, blue: Int, alpha: Int) {
    // clear screen
    // DRY - Don't repeat yourself
    // WET - Waste everyone`s time
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
}

inline fun Batch.use(action: () -> Unit) {
    begin()
    action()
    end()
}

inline fun ShapeRenderer.use(action: () -> Unit) {
    begin(ShapeRenderer.ShapeType.Line)
    action()
    end()
}

@JvmOverloads
fun ShapeRenderer.circle(c: Circle, segments: Int = 30) {
    circle(c.x, c.y, c.radius, segments)
}
