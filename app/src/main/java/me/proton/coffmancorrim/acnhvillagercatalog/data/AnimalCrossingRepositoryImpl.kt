package me.proton.coffmancorrim.acnhvillagercatalog.data

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import me.proton.coffmancorrim.acnhvillagercatalog.model.Birthday
import me.proton.coffmancorrim.acnhvillagercatalog.model.Gender
import me.proton.coffmancorrim.acnhvillagercatalog.model.Hobby
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapperDao
import me.proton.coffmancorrim.acnhvillagercatalog.model.NhDetails
import me.proton.coffmancorrim.acnhvillagercatalog.model.Personality
import me.proton.coffmancorrim.acnhvillagercatalog.model.Species
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.model.VillagerResponse
import kotlin.random.Random

class AnimalCrossingRepositoryImpl(
//    private val animalCrossingApi: AnimalCrossingApi,
    private val villagerDao: VillagerDao,
    private val listWrapperDao: ListWrapperDao
) : AnimalCrossingRepository {

    val animalCrossingApiService = RetrofitClient.apiService

    suspend fun addListWrapperIdToVillager(villagerName: String, newListWrapperId: String) {
        // Retrieve the Villager entity from the database
        val villager = villagerDao.getVillagerByName(villagerName)

        // Ensure the villager exists
        if (villager != null) {
            var currentListWrapperIds = mutableListOf<String>()
            // Get the current listWrapperIds or create an empty list if null
            if (villager.listWrapperIds != null){
                currentListWrapperIds.addAll(villager.listWrapperIds!!)
            }

            // Add the new listWrapperId to the list
            currentListWrapperIds.add(newListWrapperId)

            // Update the listWrapperIds field of the Villager entity
            villager.listWrapperIds = currentListWrapperIds

            // Update the Villager entity in the database
            villagerDao.updateVillager(villager)
        }
    }

    suspend fun cleanseVillagerWrapperIds(){
        villagerDao.removeAllListWrapperIds()
    }

    suspend fun getVillagersFromDao(favorites: Boolean = false): VillagerResponse{
        if (!villagerDao.isDaoEmpty()){
            if (!favorites){
                return VillagerResponse.Success(villagerDao.getNonFavorites())
            }
            else {
                return VillagerResponse.Success(villagerDao.getFavorites())
            }
        }
        else{
            return VillagerResponse.Error
        }
    }

    suspend fun getListWrapperDao(): List<ListWrapper>?{
        if (!listWrapperDao.isDaoEmpty()){
            return listWrapperDao.getAllLists()
        }else{
            return null
        }
    }

    suspend fun addListWrappersToDao(listWrappers: List<ListWrapper>){
        listWrapperDao.insertList(listWrappers)
    }

    suspend fun addVillagersToDao(villagerList: List<Villager>){
        villagerDao.insertAllVillagers(villagerList)
    }

    override suspend fun getVillagers(): VillagerResponse{
        val result = animalCrossingApiService.getVillagers()

        if (result.isSuccessful) {
            val villagers = result.body() ?: listOf<Villager>()
            Log.d("AnimalCrossingApi", "Villagers fetched successfully: $villagers")
            return VillagerResponse.Success(villagers)
        } else {
            Log.e("AnimalCrossingApi", "Failed to fetch villagers: ${result.message()}")
            return VillagerResponse.Error
        }


//        val jsonConfig = Json{ignoreUnknownKeys = true}
//        val villagers = jsonConfig.decodeFromString<List<Villager>>(testJson)
//        for (villager in villagers) {
//            println("Name: ${villager.name}, Species: ${villager.species}, Personality: ${villager.personality}")
//        }
//
//        val numberOfVillagers = Random.nextInt(0, 5)
//        val result = generateVillagerList(numberOfVillagers)
//
//        return if (result.isNotEmpty()) {
//            Log.d("FetchVillagers", "Villager list is not empty")
//            VillagerResponse.Success(villagers)
//        } else {
//            Log.d("FetchVillagers", "Villager list is empty")
//            VillagerResponse.Error
//        }

    }


    companion object{
    private fun generateVillagerList(numberOfVillagers: Int) : List<Villager>{
        val dummyList = mutableListOf<Villager>()
        for (i in 0 until  numberOfVillagers){
            dummyList.add(generateDummyVillager())
        }
        return dummyList
    }

        private fun generateDummyVillager(): Villager {
            val names = listOf("Bob", "Alice", "Tom", "Sally", "Charlie")
            val personalities = listOf("Big sister", "Cranky", "Jock", "Lazy", "Normal", "Peppy", "Smug", "Snooty")
            val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
            val days = (1..31).map { it.toString() }
            val signs = listOf("Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces")
            val colors = listOf("Red", "Blue", "Green", "Yellow", "Purple", "Orange", "Pink", "Brown", "Black", "White")

            val random = Random.Default

            return Villager(
                name = names.random(),
                gender = Gender.values().random(),
                personality = Personality.values().random(),
                species = Species.values().random(),
                birthdayMonth = months.random(),
                birthdayDay = days.random(),
                titleColor = generateRandomColor(),
                textColor = generateRandomColor(),
                id = "cat${random.nextInt(100)}",
                nhDetails = NhDetails(
                    iconUrl = "https://example.com/icon/${random.nextInt(100)}",
                    quote = "Random quote ${random.nextInt(100)}",
                    catchphrase = "Random catchphrase ${random.nextInt(100)}",
                    favStyles = listOf("Style1", "Style2"),
                    favColors = colors.shuffled().take(2),
                    hobby = Hobby.values().random(),
                    houseExteriorUrl = "https://example.com/house/${random.nextInt(100)}"
                )
            )
        }

        private fun generateRandomColor(): String {
            val random = Random.Default
            val color = random.nextInt(0xFFFFFF + 1)
            return String.format("#%06X", color)
        }

        val testJson = TestJsonWrapper.testJson
    }

}