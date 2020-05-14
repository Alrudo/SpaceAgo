package org.nullpointerid.spaceago

import org.nullpointerid.spaceago.entities.EntityBase
import java.io.Serializable

data class World(val entities: MutableList<EntityBase> = mutableListOf()) : Serializable