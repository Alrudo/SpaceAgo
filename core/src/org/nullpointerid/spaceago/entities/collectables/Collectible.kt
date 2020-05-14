package org.nullpointerid.spaceago.entities.collectables

import org.nullpointerid.spaceago.entities.Player
import kotlin.random.Random

interface Collectible {

    companion object {

        const val MAX_LIFE_TIME = 4f
        private const val DROP_CHANCE = 5

        fun collectibleChance(): Boolean {
            if (Random.nextInt(100) <= DROP_CHANCE) return true
            return false
        }

//        fun decideCollectible(): Class<EntityBase> {
//            return when (Random.nextInt(1, 4)) {
//                1 -> HealthPack::class.java
//                2 -> UltimateWeapon::class.java
//                else -> MoneyCrate::class.java
//            }
//        }
    }

    var lived: Float

    fun action(player: Player): Boolean
}
