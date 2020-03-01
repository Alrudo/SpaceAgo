package org.nullpointerid.spaceago.tools

fun Float.limitByRange(min: Float, max: Float): Float {
    return this.coerceAtLeast(min).coerceAtMost(max)
}