package org.nullpointerid.spaceago.entities.collectables

import org.nullpointerid.spaceago.entities.EntityBase
import org.nullpointerid.spaceago.entities.Player
import kotlin.random.Random

interface Collectible {

    companion object {

        const val MAX_LIFE_TIME = 4f
        private const val DROP_CHANCE = 5

        fun collectibleChance(): Boolean {
            if (Random.nextInt(101) <= DROP_CHANCE) return true
            return false
        }

        fun decideCollectible(): EntityBase {
            return when (Random.nextInt(1, 4)) {
                1 -> HealthPack()
                2 -> UltimateWeapon()
                else -> MoneyCrate()
            }
        }
    }

    var lived: Float

    fun action(player: Player): Boolean
}
