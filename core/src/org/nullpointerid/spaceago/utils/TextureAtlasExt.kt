package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

operator fun TextureAtlas.get(regionName: String) : TextureRegion? = findRegion(regionName)
