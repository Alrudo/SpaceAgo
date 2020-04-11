package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.assets.AssetDescriptor

inline fun <reified T : Any> assetDescriptor(fileName: String) = AssetDescriptor<T>(fileName, T::class.java)