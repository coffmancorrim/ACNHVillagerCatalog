package me.proton.coffmancorrim.acnhvillagercatalog.data

import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.model.VillagerResponse

interface AnimalCrossingRepository {
    suspend fun getVillagers(): VillagerResponse
    suspend fun addListWrapperIdToVillager(villagerName: String, newListWrapperId: String)
    suspend fun cleanseVillagerWrapperIds()
    suspend fun getVillagersFromDao(): VillagerResponse
    suspend fun getFavoriteVillagersFromDao(): VillagerResponse
    suspend fun getListWrapperDao(): List<ListWrapper>?
    suspend fun addListWrappersToDao(listWrappers: List<ListWrapper>)
    suspend fun addVillagersToDao(villagerList: List<Villager>)
}