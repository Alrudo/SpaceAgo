package org.nullpointerid.spaceago.config

object GameConfig {

    const val WIDTH = 1000  // pixels - desktop only
    const val HEIGHT = 800 // pixels - desktop only
    const val TITLE = "SpaceAgo" // desktop only

    const val HUD_WIDTH = 960f // world units: 1 pixel = 1 unit
    const val HUD_HEIGHT = 1200f // world units: 1 pixel = 1 unit

    const val WORLD_WIDTH = 9.6f // world units
    const val WORLD_HEIGHT = 12f // world units

    const val WORLD_CENTER_X = WORLD_WIDTH / 2f
    const val WORLD_CENTER_Y = WORLD_HEIGHT / 2f

    const val HEALTH_START = 10
}