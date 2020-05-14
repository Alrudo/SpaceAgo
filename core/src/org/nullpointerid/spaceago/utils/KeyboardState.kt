package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.Input
import org.nullpointerid.spaceago.SpaceShooter.STORAGE
import org.nullpointerid.spaceago.utils.gdx.isKeyPressed

class KeyboardState() {

    @Transient
    val keys = with(STORAGE) {
        mapOf(
                "moveUp" to getString("moveUp", "W"),
                "moveDown" to getString("moveDown", "S"),
                "moveLeft" to getString("moveLeft", "A"),
                "moveRight" to getString("moveRight", "D"),
                "shoot" to getString("shoot", "Space"),
                "ultimate" to getString("ultimate", "N")
        )
    }

    val isPressed: MutableList<String> = mutableListOf()

    fun isPressed(keyName: String): Boolean = isPressed.contains(keyName)

    fun update(){
        isPressed.clear()
        keys.forEach{(key, value) ->
            if(Input.Keys.valueOf(value).isKeyPressed()) isPressed.add(key)
        }
    }

}