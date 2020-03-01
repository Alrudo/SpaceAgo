package org.nullpointerid.spaceago.tools

class CollisionReact(var x: Float, var y: Float, val width: Int, val height: Int) {
    fun move(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun collidesWith(react: CollisionReact): Boolean {
        return x < react.x + react.width && y < react.y + react.height && x + width > react.x && y + height > react.y
    }

}