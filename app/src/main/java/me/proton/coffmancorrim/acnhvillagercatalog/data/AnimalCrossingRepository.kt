package me.proton.coffmancorrim.acnhvillagercatalog.data

import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.model.VillagerResponse

interface AnimalCrossingRepository {
    suspend fun getVillagers(): VillagerResponse
}