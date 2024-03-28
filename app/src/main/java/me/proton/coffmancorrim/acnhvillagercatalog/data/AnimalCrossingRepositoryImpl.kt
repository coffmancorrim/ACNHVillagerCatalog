package me.proton.coffmancorrim.acnhvillagercatalog.data

import android.util.Log
import kotlinx.serialization.json.Json
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapperDao
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.model.VillagerResponse
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
class AnimalCrossingRepositoryImpl(
//    private val animalCrossingApi: AnimalCrossingApi,
    private val villagerDao: VillagerDao,
    private val listWrapperDao: ListWrapperDao
) : AnimalCrossingRepository {
    val animalCrossingApiService = RetrofitClient.apiService

    override suspend fun getVillagers(): VillagerResponse{
        val mockResponse = true

        val result: Response<List<Villager>>
        if (!mockResponse){
            Log.d("AnimalCrossingApi", "mockResponse is false")
            result = animalCrossingApiService.getVillagers()
        }else{
            Log.d("AnimalCrossingApi", "mockResponse is true: retrieving data from test json")
            result = mockRetrofitResponse(true)
        }

        if (result.isSuccessful) {
            val villagers = result.body() ?: listOf<Villager>()
            Log.d("AnimalCrossingApi", "Villagers fetched successfully: $villagers")
            return VillagerResponse.Success(villagers)
        } else {
            Log.e("AnimalCrossingApi", "Failed to fetch villagers: ${result.message()}")
            return VillagerResponse.Error
        }
    }

    override suspend fun addListWrapperIdToVillager(villagerName: String, newListWrapperId: String) {
        val villager = villagerDao.getVillagerByName(villagerName)

        if (villager != null) {
            var currentListWrapperIds = mutableListOf<String>()
            if (villager.listWrapperIds != null){
                currentListWrapperIds.addAll(villager.listWrapperIds!!)
            }

            currentListWrapperIds.add(newListWrapperId)
            villager.listWrapperIds = currentListWrapperIds
            villagerDao.updateVillager(villager)
        }
    }

    override suspend fun cleanseVillagerWrapperIds(){
        villagerDao.removeAllListWrapperIds()
    }

    override suspend fun getVillagersFromDao(): VillagerResponse{
        if (!villagerDao.isDaoEmpty()){
            return VillagerResponse.Success(villagerDao.getNonFavorites())
        }
        else{
            return VillagerResponse.Error
        }
    }

    override suspend fun getFavoriteVillagersFromDao(): VillagerResponse{
        if (!villagerDao.isDaoEmpty()){
            return VillagerResponse.Success(villagerDao.getFavorites())
        }
        else{
            return VillagerResponse.Error
        }
    }

    override suspend fun getListWrapperDao(): List<ListWrapper>?{
        if (!listWrapperDao.isDaoEmpty()){
            return listWrapperDao.getAllLists()
        }else{
            return null
        }
    }

    override suspend fun addListWrappersToDao(listWrappers: List<ListWrapper>){
        listWrapperDao.insertList(listWrappers)
    }

    override suspend fun addVillagersToDao(villagerList: List<Villager>){
        villagerDao.insertAllVillagers(villagerList)
    }

    private fun mockRetrofitResponse(returnSuccess: Boolean): Response<List<Villager>> {
        if (returnSuccess){
            val jsonConfig = Json{ignoreUnknownKeys = true}
            val villagers = jsonConfig.decodeFromString<List<Villager>>(TestJsonWrapper.testJson)
            return Response.success(villagers)
        } else {
            val errorBody =
                "This is a mock response representing an error to the api".toResponseBody(null)
            return Response.error(404, errorBody)
        }

    }

}