package org.nullpointerid.spaceago.utils.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import org.nullpointerid.spaceago.entities.EntityBase
import java.awt.Color

inline fun <reified T : Any> assetDescriptor(fileName: String) = AssetDescriptor<T>(fileName, T::class.java)

operator fun TextureAtlas.get(regionName: String) : TextureRegion? = findRegion(regionName)

@JvmOverloads  // if you want to mix Java and Kotlin
fun clearScreen(color: Color = Color.BLACK) = clearScreen(color.red, color.green, color.blue, color.alpha)

fun clearScreen(red: Int, green: Int, blue: Int, alpha: Int) {
    // clear screen
    // DRY - Don't repeat yourself
    // WET - Waste everyone`s time
    Gdx.gl.glClearColor(red.toFloat(), green.toFloat(), blue.toFloat(), alpha.toFloat())
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

fun SpriteBatch.draw(entity: EntityBase) {
    this.draw(entity.texture(), entity.coreBound.x, entity.coreBound.y,
            entity.width / 2, entity.height / 2,
            entity.width, entity.height, 1f, 1f, entity.coreBound.rotation)
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

fun <T : Actor> T.onDrag(action: () -> Unit): T {
    addListener(object : DragListener() {
        override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
            action()
        }
    })
    return this
}

fun <T: Actor> T.onDragStop(action: () -> Unit): T {
    addListener(object: DragListener() {
        override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
            action()
        }
    })
    return this
}


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
                e.printStackTrace()
            }
        }
        start = System.currentTimeMillis()
    }
}