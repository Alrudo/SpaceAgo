package org.nullpointerid.spaceago.utils

import com.badlogic.gdx.utils.Logger

// fun <T : Any> logger(clazz: Class<T>) = Logger(clazz.simpleName, Logger.DEBUG)
inline fun <reified T : Any> logger() = Logger(T::class.java.simpleName, Logger.DEBUG)