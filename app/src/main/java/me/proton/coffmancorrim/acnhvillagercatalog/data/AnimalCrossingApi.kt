package me.proton.coffmancorrim.acnhvillagercatalog.data

import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import retrofit2.Response


interface AnimalCrossingApi {

    suspend fun getVillagers() : Response<List<Villager>>
}