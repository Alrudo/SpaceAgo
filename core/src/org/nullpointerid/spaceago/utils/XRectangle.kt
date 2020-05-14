package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.math.Polygon

class XRectangle(
        x: Float, y: Float, val width: Float, val height: Float
) : Polygon(
        floatArrayOf(0f, 0f, 0f + width, 0f, 0f + width, 0f + height, 0f, 0f + height)
) {
    constructor() : this(0f, 0f, 1f, 1f)

    var parent: XRectangle? = null
    var xOffset: Float? = null
    var yOffset: Float? = null

    init {
        setOrigin(width / 2, height / 2);
        setPosition(x, y)
    }

    fun bindToRect(rect: XRectangle, x: Float = 0f, y: Float = 0f): XRectangle {
        parent = rect
        xOffset = x
        yOffset = y
        setOrigin(rect.width / 2 - xOffset!!, rect.height / 2 - yOffset!!)
        setPosition(parent!!.x + xOffset!!, parent!!.y + yOffset!!)
        return this
    }

    fun recalculate() {
        if (parent != null && xOffset != null && yOffset != null) {
            setPosition(parent!!.x + xOffset!!, parent!!.y + yOffset!!)
            rotation = parent!!.rotation
        }
    }

    override fun getTransformedVertices(): FloatArray {
        recalculate()
        return super.getTransformedVertices()
    }
}