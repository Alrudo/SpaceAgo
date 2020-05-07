package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
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

fun ShapeRenderer.rectangle(r: Rectangle) {
    rect(r.x, r.y, r.width, r.height)
}

fun ShapeRenderer.rect(lbl: Actor) {
    this.rect(lbl.x, lbl.y, lbl.width, lbl.height)
}

fun <T : Actor> T.bind(stage: Stage): T {
    stage.addActor(this)
    return this
}

fun <T : Actor> T.extend(w: Float = 0f, h: Float = 0f): T {
    this.apply {
        width += w
        height += h
    }
    return this
}

fun <T : Actor> T.onInput(action: (event: InputEvent, keycode: Char) -> Boolean): T {
    addListener(object : InputListener() {

        override fun keyTyped(event: InputEvent, keycode: Char): Boolean {
            return action(event, keycode)
        }
    })
    return this
}

fun <T : TextField> T.filterInput(action: (textField: TextField, keycode: Char) -> Boolean): T {
    setTextFieldFilter { textField, c ->
        action(textField, c)
    }
    return this
}

fun <T : Actor> T.onClick(action: () -> Unit): T {
    addListener(object : ClickListener() {
        override fun clicked(event: InputEvent, x: Float, y: Float) {
            action()
        }
    })
    return this
}

fun inRectangle(r: Rectangle, x: Float, y: Float) = r.x <= x && r.x + r.width >= x && r.y <= y && r.y + r.height >= y


private var diff: Long = 0
private var start: Long = System.currentTimeMillis()

fun maintainFPS(fps: Int) {
    if (fps > 0) {
        diff = System.currentTimeMillis() - start
        val targetDelay = 1000 / fps.toLong()
        if (diff < targetDelay) {
            try {
                Thread.sleep(targetDelay - diff)
            } catch (e: InterruptedException) {
            }
        }
        start = System.currentTimeMillis()
    }
}