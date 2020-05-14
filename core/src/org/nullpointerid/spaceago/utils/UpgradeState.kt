package org.nullpointerid.spaceago.utils

import org.nullpointerid.spaceago.views.upgrade.UpgradeShopScreen

class UpgradeState {

    val upgrades: Map<UpgradeShopScreen.Upgrades, Int> = UpgradeShopScreen.Upgrades.values().map {
        it to it.getLevel()
    }.toMap()

    fun getLevel(upgrade: UpgradeShopScreen.Upgrades): Int {
        return upgrades.getOrDefault(upgrade, 0)
    }
}