package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import org.nullpointerid.spaceago.SpaceShooter
import org.nullpointerid.spaceago.utils.gdx.toInternalFile

object Audio {

    var volume: Float = SpaceShooter.STORAGE.getFloat("volume", 0.5f)
    var shotSound: Sound = Gdx.audio.newSound("audio/shotSound.mp3".toInternalFile())
    var explosionSound: Sound = Gdx.audio.newSound("audio/enemyExplosionSound.mp3".toInternalFile())

}