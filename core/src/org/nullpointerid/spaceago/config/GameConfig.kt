package org.nullpointerid.spaceago.config

object GameConfig {

    const val DEBUG_MODE = false

    const val WIDTH = 1000  // pixels - desktop only
    const val HEIGHT = 800 // pixels - desktop only
    const val TITLE = "SpaceAgo" // desktop only

    const val HUD_WIDTH = 1000f // world units: 1 pixel = 1 unit
    const val HUD_HEIGHT = 800f // world units: 1 pixel = 1 unit

    const val WORLD_WIDTH = 10f // world units
    const val WORLD_HEIGHT = 8f // world units

    const val WORLD_CENTER_X = WORLD_WIDTH / 2f
    const val WORLD_CENTER_Y = WORLD_HEIGHT / 2f

    const val MAX_ENEMY_SPAWN_TIME = 0.45f
    const val MIN_ENEMY_SPAWN_TIME = 0.2f

    const val LIVES_START = 1f
}