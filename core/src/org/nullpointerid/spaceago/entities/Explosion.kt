package org.nullpointerid.spaceago.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import org.nullpointerid.spaceago.assets.AssetPaths

import org.nullpointerid.spaceago.utils.GdxArray
import org.nullpointerid.spaceago.utils.toInternalFile

class Explosion : EntityBase() {

    companion object {
        private const val TILE_WIDTH = 32
        private const val TILE_HEIGHT = 32

        const val TEXTURE_WIDTH = 1f
        const val TEXTURE_HEIGHT = 0.8f
    }

    private val texture = Texture(AssetPaths.EXPLOSION.toInternalFile())

    var stateTime = 0f
    val animation = Animation(0.2f, *TextureRegion.split(texture, TILE_WIDTH, TILE_HEIGHT)[0])

    override val bounds = GdxArray<Rectangle>()

    override fun updateBounds() {}
}