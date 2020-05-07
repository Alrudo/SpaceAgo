package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.math.Rectangle

interface Collidable {

    val bounds: MutableList<Rectangle>

    fun isColliding(){

    }

}