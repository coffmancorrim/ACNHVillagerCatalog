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
//        val result = animalCrossingApi.getVillagers()
//
//        if (result.isSuccessful){
//            return VillagerResponse.Success(result.body() ?: listOf<Villager>())
//        }else{
//            return VillagerResponse.Error
//        }

        val jsonConfig = Json{ignoreUnknownKeys = true}
        val villagers = jsonConfig.decodeFromString<List<Villager>>(testJson)
        for (villager in villagers) {
            println("Name: ${villager.name}, Species: ${villager.species}, Personality: ${villager.personality}")
        }

        val numberOfVillagers = Random.nextInt(0, 5)
        val result = generateVillagerList(numberOfVillagers)

        Log.d("FetchVillagers", "Generating villager list")

        delay(1000)
        Log.d("FetchVillagers", "Delay completed")

        return if (result.isNotEmpty()) {
            Log.d("FetchVillagers", "Villager list is not empty")
            VillagerResponse.Success(villagers)
        } else {
            Log.d("FetchVillagers", "Villager list is empty")
            VillagerResponse.Error
        }

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

        val testJson = """[
    {
        "name": "Biskit",
        "url": "https://nookipedia.com/wiki/Biskit",
        "alt_name": "",
        "title_color": "ffaa3b",
        "text_color": "874c25",
        "id": "dog03",
        "image_url": "https://dodo.ac/np/images/7/78/Biskit_NH.png",
        "species": "Dog",
        "personality": "Lazy",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "13",
        "sign": "Taurus",
        "quote": "Let sleeping dogs lie.",
        "phrase": "dawg",
        "clothing": "meme shirt",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/7/78/Biskit_NH.png",
            "photo_url": "https://dodo.ac/np/images/c/cc/Biskit%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/2/25/Biskit_NH_Villager_Icon.png",
            "quote": "Let sleeping dogs lie.",
            "sub-personality": "B",
            "catchphrase": "dawg",
            "clothing": "meme shirt",
            "clothing_variation": "Purple",
            "fav_styles": [
                "Gorgeous",
                "Simple"
            ],
            "fav_colors": [
                "Purple",
                "Colorful"
            ],
            "hobby": "Play",
            "house_interior_url": "https://dodo.ac/np/images/c/ca/House_of_Biskit_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/6/6e/House_of_Biskit_NH_Model.png",
            "house_wallpaper": "Backyard-Fence Wall",
            "house_flooring": "Backyard Lawn",
            "house_music": "K.K. Mambo",
            "house_music_note": ""
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WW",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Bruce",
        "url": "https://nookipedia.com/wiki/Bruce",
        "alt_name": "",
        "title_color": "459aba",
        "text_color": "fffce9",
        "id": "der03",
        "image_url": "https://dodo.ac/np/images/0/08/Bruce_NH.png",
        "species": "Deer",
        "personality": "Cranky",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "26",
        "sign": "Gemini",
        "quote": "Nobody's perfect.",
        "phrase": "gruff",
        "clothing": "after-school jacket",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/0/08/Bruce_NH.png",
            "photo_url": "https://dodo.ac/np/images/b/bd/Bruce%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/9/9b/Bruce_NH_Villager_Icon.png",
            "quote": "Nobody's perfect.",
            "sub-personality": "A",
            "catchphrase": "gruff",
            "clothing": "after-school jacket",
            "clothing_variation": "Black",
            "fav_styles": [
                "Cool",
                "Simple"
            ],
            "fav_colors": [
                "Black",
                "Red"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/b/b5/House_of_Bruce_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/9/91/House_of_Bruce_NH_Model.png",
            "house_wallpaper": "Street-Art Wall",
            "house_flooring": "Paintball Flooring",
            "house_music": "K.K. Blues",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Bunnie",
        "url": "https://nookipedia.com/wiki/Bunnie",
        "alt_name": "",
        "title_color": "ff791f",
        "text_color": "fff2bb",
        "id": "rbt00",
        "image_url": "https://dodo.ac/np/images/0/00/Bunnie_NH_Transparent.png",
        "species": "Rabbit",
        "personality": "Peppy",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "9",
        "sign": "Taurus",
        "quote": "Hare today, gone tomorrow.",
        "phrase": "tee-hee",
        "clothing": "lively plaid dress",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/b/b2/Bunnie_NH.png",
            "photo_url": "https://dodo.ac/np/images/e/ef/Bunnie%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/f/f5/Bunnie_NH_Villager_Icon.png",
            "quote": "Hare today, gone tomorrow.",
            "sub-personality": "B",
            "catchphrase": "tee-hee",
            "clothing": "lively plaid dress",
            "clothing_variation": "",
            "fav_styles": [
                "Cute"
            ],
            "fav_colors": [
                "Green",
                "Pink"
            ],
            "hobby": "Fashion",
            "house_interior_url": "https://dodo.ac/np/images/f/f9/House_of_Bunnie_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/6/62/House_of_Bunnie_NH_Model.png",
            "house_wallpaper": "Purple Dotted Wall",
            "house_flooring": "White-Paint Flooring",
            "house_music": "Forest Life",
            "house_music_note": ""
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WW",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Canberra",
        "url": "https://nookipedia.com/wiki/Canberra",
        "alt_name": "",
        "title_color": "ff791f",
        "text_color": "fff2bb",
        "id": "kal08",
        "image_url": "https://dodo.ac/np/images/e/e6/Canberra_NH.png",
        "species": "Koala",
        "personality": "Sisterly",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "14",
        "sign": "Taurus",
        "quote": "It's never too late to start over.",
        "phrase": "nuh uh",
        "clothing": "striped tank",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/e/e6/Canberra_NH.png",
            "photo_url": "https://dodo.ac/np/images/9/9d/Canberra%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/6/62/Canberra_NH_Villager_Icon.png",
            "quote": "It's never too late to start over.",
            "sub-personality": "A",
            "catchphrase": "nuh uh",
            "clothing": "striped tank",
            "clothing_variation": "Green",
            "fav_styles": [
                "Active",
                "Cool"
            ],
            "fav_colors": [
                "Green",
                "Aqua"
            ],
            "hobby": "Play",
            "house_interior_url": "https://dodo.ac/np/images/1/17/House_of_Canberra_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/5/5b/House_of_Canberra_NH_Model.png",
            "house_wallpaper": "Tropical Vista",
            "house_flooring": "Starry-Sands Flooring",
            "house_music": "K.K. Island",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Cherry",
        "url": "https://nookipedia.com/wiki/Cherry",
        "alt_name": "",
        "title_color": "ff6183",
        "text_color": "fffce9",
        "id": "dog17",
        "image_url": "https://dodo.ac/np/images/3/3e/Cherry_NH.png",
        "species": "Dog",
        "personality": "Sisterly",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "11",
        "sign": "Taurus",
        "quote": "One dog barks at something, the rest bark at him.",
        "phrase": "what what",
        "clothing": "spider-web tee",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/3/3e/Cherry_NH.png",
            "photo_url": "https://dodo.ac/np/images/d/de/Cherry%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/7/73/Cherry_NH_Villager_Icon.png",
            "quote": "One dog barks at something, the rest bark at him.",
            "sub-personality": "A",
            "catchphrase": "what what",
            "clothing": "spider-web tee",
            "clothing_variation": "",
            "fav_styles": [
                "Cool",
                "Elegant"
            ],
            "fav_colors": [
                "Black",
                "Purple"
            ],
            "hobby": "Music",
            "house_interior_url": "https://dodo.ac/np/images/f/f8/House_of_Cherry_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/6/64/House_of_Cherry_NH_Model.png",
            "house_wallpaper": "Cityscape Wall",
            "house_flooring": "Skull-Print Flooring",
            "house_music": "K.K. D&B",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Clyde",
        "url": "https://nookipedia.com/wiki/Clyde",
        "alt_name": "",
        "title_color": "d8cc39",
        "text_color": "8b5f57",
        "id": "hrs10",
        "image_url": "https://dodo.ac/np/images/b/bb/Clyde_NH.png",
        "species": "Horse",
        "personality": "Lazy",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "1",
        "sign": "Taurus",
        "quote": "The grass is always greener.",
        "phrase": "clip clawp",
        "clothing": "madras plaid shirt",
        "islander": false,
        "debut": "CF",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/b/bb/Clyde_NH.png",
            "photo_url": "https://dodo.ac/np/images/c/c0/Clyde%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/5/5a/Clyde_NH_Villager_Icon.png",
            "quote": "The grass is always greener.",
            "sub-personality": "B",
            "catchphrase": "clip clawp",
            "clothing": "madras plaid shirt",
            "clothing_variation": "Pink",
            "fav_styles": [
                "Simple",
                "Cute"
            ],
            "fav_colors": [
                "Green",
                "White"
            ],
            "hobby": "Play",
            "house_interior_url": "https://dodo.ac/np/images/0/07/House_of_Clyde_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/a/a5/House_of_Clyde_NH_Model.png",
            "house_wallpaper": "Orange Wall",
            "house_flooring": "Birch Flooring",
            "house_music": "Neapolitan",
            "house_music_note": ""
        },
        "appearances": [
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Colton",
        "url": "https://nookipedia.com/wiki/Colton",
        "alt_name": "",
        "title_color": "ffffff",
        "text_color": "848484",
        "id": "hrs11",
        "image_url": "https://dodo.ac/np/images/8/8c/Colton_NH.png",
        "species": "Horse",
        "personality": "Smug",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "22",
        "sign": "Gemini",
        "quote": "Make hay while the sun shines.",
        "phrase": "check it",
        "clothing": "prince's tunic",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/8/8c/Colton_NH.png",
            "photo_url": "https://dodo.ac/np/images/3/3a/Colton%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/c/c5/Colton_NH_Villager_Icon.png",
            "quote": "Make hay while the sun shines.",
            "sub-personality": "A",
            "catchphrase": "check it",
            "clothing": "prince's tunic",
            "clothing_variation": "Blue",
            "fav_styles": [
                "Gorgeous",
                "Elegant"
            ],
            "fav_colors": [
                "Blue",
                "Red"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/f/f4/House_of_Colton_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/2/21/House_of_Colton_NH_Model.png",
            "house_wallpaper": "Palace Wall",
            "house_flooring": "Palace Tile",
            "house_music": "K.K. Chorale",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Curlos",
        "url": "https://nookipedia.com/wiki/Curlos",
        "alt_name": "",
        "title_color": "ffd00d",
        "text_color": "9b553a",
        "id": "shp08",
        "image_url": "https://dodo.ac/np/images/1/1a/Curlos_NH_Transparent.png",
        "species": "Sheep",
        "personality": "Smug",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "8",
        "sign": "Taurus",
        "quote": "If you want to know yourself better, ask your neighbors.",
        "phrase": "shearly",
        "clothing": "zigzag shirt",
        "islander": true,
        "debut": "E_PLUS",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/3/37/Curlos_NH.png",
            "photo_url": "https://dodo.ac/np/images/1/14/Curlos%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/d/d0/Curlos_NH_Villager_Icon.png",
            "quote": "If you want to know yourself better, ask your neighbors.",
            "sub-personality": "B",
            "catchphrase": "shearly",
            "clothing": "zigzag shirt",
            "clothing_variation": "",
            "fav_styles": [
                "Active",
                "Gorgeous"
            ],
            "fav_colors": [
                "Red",
                "Green"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/7/74/House_of_Curlos_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/1/16/House_of_Curlos_NH_Model.png",
            "house_wallpaper": "Wild-Wood Wall",
            "house_flooring": "Dark Herringbone Flooring",
            "house_music": "K.K. Salsa",
            "house_music_note": ""
        },
        "appearances": [
            "E_PLUS",
            "NL",
            "WA",
            "NH",
            "HHD"
        ]
    },
    {
        "name": "Deirdre",
        "url": "https://nookipedia.com/wiki/Deirdre",
        "alt_name": "",
        "title_color": "ddcdca",
        "text_color": "4b4496",
        "id": "der04",
        "image_url": "https://dodo.ac/np/images/3/3c/Deirdre_NH.png",
        "species": "Deer",
        "personality": "Sisterly",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "4",
        "sign": "Taurus",
        "quote": "Legs of an antelope, heart of an eagle.",
        "phrase": "whatevs",
        "clothing": "flower sweater",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/3/3c/Deirdre_NH.png",
            "photo_url": "https://dodo.ac/np/images/0/0f/Deirdre%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/3/3a/Deirdre_NH_Villager_Icon.png",
            "quote": "Legs of an antelope, heart of an eagle.",
            "sub-personality": "A",
            "catchphrase": "whatevs",
            "clothing": "flower sweater",
            "clothing_variation": "Yellow",
            "fav_styles": [
                "Simple"
            ],
            "fav_colors": [
                "Orange"
            ],
            "hobby": "Play",
            "house_interior_url": "https://dodo.ac/np/images/e/e6/House_of_Deirdre_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/3/3d/House_of_Deirdre_NH_Model.png",
            "house_wallpaper": "Autumn Wall",
            "house_flooring": "Colored-Leaves Flooring",
            "house_music": "K.K. Bossa",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Del",
        "url": "https://nookipedia.com/wiki/Del",
        "alt_name": "",
        "title_color": "6b75ce",
        "text_color": "9ae8df",
        "id": "crd04",
        "image_url": "https://dodo.ac/np/images/4/46/Del_NH.png",
        "species": "Alligator",
        "personality": "Cranky",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "27",
        "sign": "Gemini",
        "quote": "The hero is brave in deeds as well as words.",
        "phrase": "gronk",
        "clothing": "striped shirt",
        "islander": false,
        "debut": "E_PLUS",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/4/46/Del_NH.png",
            "photo_url": "https://dodo.ac/np/images/7/7d/Del%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/8/8b/Del_NH_Villager_Icon.png",
            "quote": "The hero is brave in deeds as well as words.",
            "sub-personality": "B",
            "catchphrase": "gronk",
            "clothing": "striped shirt",
            "clothing_variation": "Blue",
            "fav_styles": [
                "Cool",
                "Simple"
            ],
            "fav_colors": [
                "Blue",
                "White"
            ],
            "hobby": "Fitness",
            "house_interior_url": "https://dodo.ac/np/images/5/5c/House_of_Del_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/4/4c/House_of_Del_NH_Model.png",
            "house_wallpaper": "Industrial Wall",
            "house_flooring": "Ship Deck",
            "house_music": "Pondering",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "E_PLUS",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Deli",
        "url": "https://nookipedia.com/wiki/Deli",
        "alt_name": "",
        "title_color": "c0ab72",
        "text_color": "fffce9",
        "id": "mnk08",
        "image_url": "https://dodo.ac/np/images/3/30/Deli_NH.png",
        "species": "Monkey",
        "personality": "Lazy",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "24",
        "sign": "Gemini",
        "quote": "Righty tighty, lefty loosey.",
        "phrase": "monch",
        "clothing": "argyle vest",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/3/30/Deli_NH.png",
            "photo_url": "https://dodo.ac/np/images/9/9f/Deli%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/d/da/Deli_NH_Villager_Icon.png",
            "quote": "Righty tighty, lefty loosey.",
            "sub-personality": "A",
            "catchphrase": "monch",
            "clothing": "argyle vest",
            "clothing_variation": "Blue",
            "fav_styles": [
                "Gorgeous",
                "Elegant"
            ],
            "fav_colors": [
                "Purple",
                "Brown"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/b/b0/House_of_Deli_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/7/74/House_of_Deli_NH_Model.png",
            "house_wallpaper": "Blue Simple-Cloth Wall",
            "house_flooring": "Simple Blue Flooring",
            "house_music": "K.K. Étude",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Derwin",
        "url": "https://nookipedia.com/wiki/Derwin",
        "alt_name": "",
        "title_color": "0961f6",
        "text_color": "fffce9",
        "id": "duk08",
        "image_url": "https://dodo.ac/np/images/9/98/Derwin_NH.png",
        "species": "Duck",
        "personality": "Lazy",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "25",
        "sign": "Gemini",
        "quote": "Everything in moderation. Except for snacks.",
        "phrase": "derrrr",
        "clothing": "striped tank",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/9/98/Derwin_NH.png",
            "photo_url": "https://dodo.ac/np/images/1/12/Derwin%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/6/62/Derwin_NH_Villager_Icon.png",
            "quote": "Everything in moderation. Except for snacks.",
            "sub-personality": "B",
            "catchphrase": "derrrr",
            "clothing": "striped tank",
            "clothing_variation": "Yellow",
            "fav_styles": [
                "Simple",
                "Elegant"
            ],
            "fav_colors": [
                "Yellow",
                "Beige"
            ],
            "hobby": "Play",
            "house_interior_url": "https://dodo.ac/np/images/6/66/House_of_Derwin_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/f/ff/House_of_Derwin_NH_Model.png",
            "house_wallpaper": "Backyard-Fence Wall",
            "house_flooring": "Backyard Lawn",
            "house_music": "K.K. Stroll",
            "house_music_note": ""
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WW",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Ellie",
        "url": "https://nookipedia.com/wiki/Ellie",
        "alt_name": "",
        "title_color": "c0ab72",
        "text_color": "fffce9",
        "id": "elp07",
        "image_url": "https://dodo.ac/np/images/3/38/Ellie_NH.png",
        "species": "Elephant",
        "personality": "Normal",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "12",
        "sign": "Taurus",
        "quote": "You need the sour to highlight the sweet.",
        "phrase": "li'l one",
        "clothing": "Aran-knit sweater",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [
            "wee one"
        ],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/3/38/Ellie_NH.png",
            "photo_url": "https://dodo.ac/np/images/3/37/Ellie%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/b/b3/Ellie_NH_Villager_Icon.png",
            "quote": "You need the sour to highlight the sweet.",
            "sub-personality": "A",
            "catchphrase": "li'l one",
            "clothing": "Aran-knit sweater",
            "clothing_variation": "Red",
            "fav_styles": [
                "Simple",
                "Cute"
            ],
            "fav_colors": [
                "Gray",
                "Pink"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/7/7c/House_of_Ellie_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/0/00/House_of_Ellie_NH_Model.png",
            "house_wallpaper": "Beige Art-Deco Wall",
            "house_flooring": "Rose Flooring",
            "house_music": "K.K. Ballad",
            "house_music_note": ""
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WA",
            "NH",
            "PC"
        ]
    },
    {
        "name": "Gayle",
        "url": "https://nookipedia.com/wiki/Gayle",
        "alt_name": "",
        "title_color": "f993ce",
        "text_color": "fffce9",
        "id": "crd07",
        "image_url": "https://dodo.ac/np/images/5/5e/Gayle_NH.png",
        "species": "Alligator",
        "personality": "Normal",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "17",
        "sign": "Taurus",
        "quote": "An ounce of prevention is worth a pound of cure.",
        "phrase": "snacky",
        "clothing": "lace-up dress",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/5/5e/Gayle_NH.png",
            "photo_url": "https://dodo.ac/np/images/1/16/Gayle%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/2/20/Gayle_NH_Villager_Icon.png",
            "quote": "An ounce of prevention is worth a pound of cure.",
            "sub-personality": "A",
            "catchphrase": "snacky",
            "clothing": "lace-up dress",
            "clothing_variation": "Pink",
            "fav_styles": [
                "Cute"
            ],
            "fav_colors": [
                "Pink",
                "White"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/7/76/House_of_Gayle_NH.png",
            "house_exterior_url": "https://dodo.ac/np/images/6/60/House_of_Gayle_NH_Model.png",
            "house_wallpaper": "Pink Quilt Wall",
            "house_flooring": "Pine-Board Flooring",
            "house_music": "Bubblegum K.K.",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Hamlet",
        "url": "https://nookipedia.com/wiki/Hamlet",
        "alt_name": "",
        "title_color": "ffd00d",
        "text_color": "9b553a",
        "id": "ham00",
        "image_url": "https://dodo.ac/np/images/4/46/Hamlet_NH.png",
        "species": "Hamster",
        "personality": "Jock",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "30",
        "sign": "Gemini",
        "quote": "The ear's the thing.",
        "phrase": "hammie",
        "clothing": "big-star tee",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/4/46/Hamlet_NH.png",
            "photo_url": "https://dodo.ac/np/images/0/09/Hamlet%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/a/a0/Hamlet_NH_Villager_Icon.png",
            "quote": "The ear's the thing.",
            "sub-personality": "A",
            "catchphrase": "hammie",
            "clothing": "big-star tee",
            "clothing_variation": "",
            "fav_styles": [
                "Simple",
                "Active"
            ],
            "fav_colors": [
                "Purple",
                "Blue"
            ],
            "hobby": "Play",
            "house_interior_url": "https://dodo.ac/np/images/2/2f/House_of_Hamlet_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/4/41/House_of_Hamlet_NH_Model.png",
            "house_wallpaper": "Blue Playroom Wall",
            "house_flooring": "Colorful Puzzle Flooring",
            "house_music": "K.K. Reggae",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Ike",
        "url": "https://nookipedia.com/wiki/Ike",
        "alt_name": "",
        "title_color": "4c3317",
        "text_color": "fffce9",
        "id": "bea11",
        "image_url": "https://dodo.ac/np/images/3/34/Ike_NH.png",
        "species": "Bear",
        "personality": "Cranky",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "16",
        "sign": "Taurus",
        "quote": "The higher you climb, the smaller things look.",
        "phrase": "roadie",
        "clothing": "camo bomber-style jacket",
        "islander": false,
        "debut": "E_PLUS",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/3/34/Ike_NH.png",
            "photo_url": "https://dodo.ac/np/images/3/3a/Ike%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/c/c9/Ike_NH_Villager_Icon.png",
            "quote": "The higher you climb, the smaller things look.",
            "sub-personality": "A",
            "catchphrase": "roadie",
            "clothing": "camo bomber-style jacket",
            "clothing_variation": "Green",
            "fav_styles": [
                "Cool"
            ],
            "fav_colors": [
                "Green",
                "Blue"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/f/f6/House_of_Ike_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/c/cd/House_of_Ike_NH_Model.png",
            "house_wallpaper": "Shutter Wall",
            "house_flooring": "Steel Flooring",
            "house_music": "K.K. Rock",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "E_PLUS",
            "WA",
            "NH",
            "PC"
        ]
    },
    {
        "name": "June",
        "url": "https://nookipedia.com/wiki/June",
        "alt_name": "",
        "title_color": "c0ab72",
        "text_color": "fffce9",
        "id": "cbr13",
        "image_url": "https://dodo.ac/np/images/8/85/June_NH.png",
        "species": "Cub",
        "personality": "Normal",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "21",
        "sign": "Gemini",
        "quote": "Dream big, expect little.",
        "phrase": "rainbow",
        "clothing": "hibiscus muumuu",
        "islander": true,
        "debut": "AC",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/8/85/June_NH.png",
            "photo_url": "https://dodo.ac/np/images/6/6c/June%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/2/29/June_NH_Villager_Icon.png",
            "quote": "Dream big, expect little.",
            "sub-personality": "A",
            "catchphrase": "rainbow",
            "clothing": "hibiscus muumuu",
            "clothing_variation": "Red",
            "fav_styles": [
                "Cute",
                "Simple"
            ],
            "fav_colors": [
                "White",
                "Red"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/2/24/House_of_June_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/6/68/House_of_June_NH_Model.png",
            "house_wallpaper": "Ocean-Horizon Wall",
            "house_flooring": "Starry-Sands Flooring",
            "house_music": "K.K. Island",
            "house_music_note": ""
        },
        "appearances": [
            "AC",
            "E_PLUS",
            "WA",
            "NH",
            "PC"
        ]
    },
    {
        "name": "Leonardo",
        "url": "https://nookipedia.com/wiki/Leonardo",
        "alt_name": "",
        "title_color": "ffd00d",
        "text_color": "9b553a",
        "id": "tig04",
        "image_url": "https://dodo.ac/np/images/0/0a/Leonardo_NH.png",
        "species": "Tiger",
        "personality": "Jock",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "15",
        "sign": "Taurus",
        "quote": "That's the way the energy bar crumbles.",
        "phrase": "flexin'",
        "clothing": "hawk jacket",
        "islander": false,
        "debut": "E_PLUS",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/0/0a/Leonardo_NH.png",
            "photo_url": "https://dodo.ac/np/images/3/3d/Leonardo%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/c/ca/Leonardo_NH_Villager_Icon.png",
            "quote": "That's the way the energy bar crumbles.",
            "sub-personality": "B",
            "catchphrase": "flexin'",
            "clothing": "hawk jacket",
            "clothing_variation": "",
            "fav_styles": [
                "Active",
                "Gorgeous"
            ],
            "fav_colors": [
                "Red",
                "Blue"
            ],
            "hobby": "Fitness",
            "house_interior_url": "https://dodo.ac/np/images/a/a8/House_of_Leonardo_NH.png",
            "house_exterior_url": "https://dodo.ac/np/images/9/9c/House_of_Leonardo_NH_Model.png",
            "house_wallpaper": "Skull Wall",
            "house_flooring": "Leopard-Print Flooring",
            "house_music": "K.K. Metal",
            "house_music_note": ""
        },
        "appearances": [
            "E_PLUS",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Marcie",
        "url": "https://nookipedia.com/wiki/Marcie",
        "alt_name": "",
        "title_color": "f993ce",
        "text_color": "fffce9",
        "id": "kgr10",
        "image_url": "https://dodo.ac/np/images/2/2b/Marcie_NH.png",
        "species": "Kangaroo",
        "personality": "Normal",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "31",
        "sign": "Gemini",
        "quote": "The road to a friend's house is never long.",
        "phrase": "pouches",
        "clothing": "heart apron",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/2/2b/Marcie_NH.png",
            "photo_url": "https://dodo.ac/np/images/e/e0/Marcie%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/d/d5/Marcie_NH_Villager_Icon.png",
            "quote": "The road to a friend's house is never long.",
            "sub-personality": "A",
            "catchphrase": "pouches",
            "clothing": "heart apron",
            "clothing_variation": "Pink",
            "fav_styles": [
                "Cute",
                "Elegant"
            ],
            "fav_colors": [
                "Pink",
                "Beige"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/a/a6/House_of_Marcie_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/9/90/House_of_Marcie_NH_Model.png",
            "house_wallpaper": "Pink Quilt Wall",
            "house_flooring": "Pine-Board Flooring",
            "house_music": "K.K. Aria",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Mint",
        "url": "https://nookipedia.com/wiki/Mint",
        "alt_name": "",
        "title_color": "87e0a9",
        "text_color": "219479",
        "id": "squ09",
        "image_url": "https://dodo.ac/np/images/5/5b/Mint_NH.png",
        "species": "Squirrel",
        "personality": "Snooty",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "2",
        "sign": "Taurus",
        "quote": "Always lead by example.",
        "phrase": "ahhhhhh",
        "clothing": "gumdrop dress",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/5/5b/Mint_NH.png",
            "photo_url": "https://dodo.ac/np/images/2/25/Mint%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/b/bf/Mint_NH_Villager_Icon.png",
            "quote": "Always lead by example.",
            "sub-personality": "B",
            "catchphrase": "ahhhhhh",
            "clothing": "gumdrop dress",
            "clothing_variation": "Pop",
            "fav_styles": [
                "Gorgeous",
                "Cute"
            ],
            "fav_colors": [
                "Pink",
                "Purple"
            ],
            "hobby": "Fashion",
            "house_interior_url": "https://dodo.ac/np/images/9/96/House_of_Mint_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/b/b1/House_of_Mint_NH_Model.png",
            "house_wallpaper": "Blue Subway-Tile Wall",
            "house_flooring": "Mint Dot Flooring",
            "house_music": "K.K. Soul",
            "house_music_note": ""
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WW",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Olaf",
        "url": "https://nookipedia.com/wiki/Olaf",
        "alt_name": "",
        "title_color": "ff791f",
        "text_color": "fff2bb",
        "id": "ant09",
        "image_url": "https://dodo.ac/np/images/1/11/Olaf_NH.png",
        "species": "Anteater",
        "personality": "Smug",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "19",
        "sign": "Taurus",
        "quote": "Keep your nose clean.",
        "phrase": "whiffa",
        "clothing": "suit of lights",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/1/11/Olaf_NH.png",
            "photo_url": "https://dodo.ac/np/images/c/c3/Olaf%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/7/78/Olaf_NH_Villager_Icon.png",
            "quote": "Keep your nose clean.",
            "sub-personality": "A",
            "catchphrase": "whiffa",
            "clothing": "suit of lights",
            "clothing_variation": "Black",
            "fav_styles": [
                "Elegant",
                "Gorgeous"
            ],
            "fav_colors": [
                "Red",
                "Black"
            ],
            "hobby": "Education",
            "house_interior_url": "https://dodo.ac/np/images/2/22/House_of_Olaf_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/d/d8/House_of_Olaf_NH_Model.png",
            "house_wallpaper": "Black-Brick Wall",
            "house_flooring": "Simple Red Flooring",
            "house_music": "K.K. Milonga",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Ozzie",
        "url": "https://nookipedia.com/wiki/Ozzie",
        "alt_name": "",
        "title_color": "bfbfbf",
        "text_color": "5e5e5e",
        "id": "kal05",
        "image_url": "https://dodo.ac/np/images/3/3a/Ozzie_NH.png",
        "species": "Koala",
        "personality": "Lazy",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "7",
        "sign": "Taurus",
        "quote": "Half a loaf is better than none.",
        "phrase": "ol' bear",
        "clothing": "energetic sweater",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/3/3a/Ozzie_NH.png",
            "photo_url": "https://dodo.ac/np/images/4/40/Ozzie%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/c/cb/Ozzie_NH_Villager_Icon.png",
            "quote": "Half a loaf is better than none.",
            "sub-personality": "B",
            "catchphrase": "ol' bear",
            "clothing": "energetic sweater",
            "clothing_variation": "Yellow",
            "fav_styles": [
                "Simple",
                "Cute"
            ],
            "fav_colors": [
                "Yellow",
                "Orange"
            ],
            "hobby": "Play",
            "house_interior_url": "https://dodo.ac/np/images/8/87/House_of_Ozzie_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/5/59/House_of_Ozzie_NH_Model.png",
            "house_wallpaper": "White Perforated-Board Wall",
            "house_flooring": "Cork Flooring",
            "house_music": "Mr. K.K.",
            "house_music_note": ""
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Paolo",
        "url": "https://nookipedia.com/wiki/Paolo",
        "alt_name": "",
        "title_color": "f993ce",
        "text_color": "fffce9",
        "id": "elp05",
        "image_url": "https://dodo.ac/np/images/5/59/Paolo_NH.png",
        "species": "Elephant",
        "personality": "Lazy",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "5",
        "sign": "Taurus",
        "quote": "When it works, it works.",
        "phrase": "pal",
        "clothing": "simple parka",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/5/59/Paolo_NH.png",
            "photo_url": "https://dodo.ac/np/images/d/d3/Paolo%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/d/df/Paolo_NH_Villager_Icon.png",
            "quote": "When it works, it works.",
            "sub-personality": "A",
            "catchphrase": "pal",
            "clothing": "simple parka",
            "clothing_variation": "Gray",
            "fav_styles": [
                "Simple",
                "Elegant"
            ],
            "fav_colors": [
                "Gray",
                "Aqua"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/e/e4/House_of_Paolo_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/2/2f/House_of_Paolo_NH_Model.png",
            "house_wallpaper": "Blue Moroccan Wall",
            "house_flooring": "Blue Mosaic-Tile Flooring",
            "house_music": "To the Edge",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WA",
            "NH",
            "PC"
        ]
    },
    {
        "name": "Patty",
        "url": "https://nookipedia.com/wiki/Patty",
        "alt_name": "",
        "title_color": "7a2500",
        "text_color": "fffce9",
        "id": "cow00",
        "image_url": "https://dodo.ac/np/images/f/f1/Patty_NH.png",
        "species": "Cow",
        "personality": "Peppy",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "10",
        "sign": "Taurus",
        "quote": "A friend in need is a friend indeed!",
        "phrase": "how-now",
        "clothing": "orange dress",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [
            "how now"
        ],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/f/f1/Patty_NH.png",
            "photo_url": "https://dodo.ac/np/images/8/84/Patty%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/f/fe/Patty_NH_Villager_Icon.png",
            "quote": "A friend in need is a friend indeed!",
            "sub-personality": "B",
            "catchphrase": "how-now",
            "clothing": "orange dress",
            "clothing_variation": "",
            "fav_styles": [
                "Simple",
                "Cute"
            ],
            "fav_colors": [
                "Orange",
                "Red"
            ],
            "hobby": "Fashion",
            "house_interior_url": "https://dodo.ac/np/images/7/77/House_of_Patty_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/e/ea/House_of_Patty_NH_Model.png",
            "house_wallpaper": "Backyard-Fence Wall",
            "house_flooring": "Wildflower Meadow",
            "house_music": "K.K. Calypso",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WW",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Peggy",
        "url": "https://nookipedia.com/wiki/Peggy",
        "alt_name": "",
        "title_color": "e4b887",
        "text_color": "fffce9",
        "id": "pig11",
        "image_url": "https://dodo.ac/np/images/2/28/Peggy_NH.png",
        "species": "Pig",
        "personality": "Peppy",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "23",
        "sign": "Gemini",
        "quote": "We're all diamonds in the rough.",
        "phrase": "shweetie",
        "clothing": "Aran-knit sweater",
        "islander": false,
        "debut": "E_PLUS",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/2/28/Peggy_NH.png",
            "photo_url": "https://dodo.ac/np/images/9/99/Peggy%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/d/d9/Peggy_NH_Villager_Icon.png",
            "quote": "We're all diamonds in the rough.",
            "sub-personality": "B",
            "catchphrase": "shweetie",
            "clothing": "Aran-knit sweater",
            "clothing_variation": "Red",
            "fav_styles": [
                "Cute",
                "Active"
            ],
            "fav_colors": [
                "Blue",
                "Red"
            ],
            "hobby": "Fashion",
            "house_interior_url": "https://dodo.ac/np/images/9/9b/House_of_Peggy_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/a/a5/House_of_Peggy_NH_Model.png",
            "house_wallpaper": "Red Intricate Wall",
            "house_flooring": "Birch Flooring",
            "house_music": "K.K. Bossa",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "E_PLUS",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Pekoe",
        "url": "https://nookipedia.com/wiki/Pekoe",
        "alt_name": "",
        "title_color": "ffffff",
        "text_color": "848484",
        "id": "cbr14",
        "image_url": "https://dodo.ac/np/images/d/df/Pekoe_NH.png",
        "species": "Cub",
        "personality": "Normal",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "18",
        "sign": "Taurus",
        "quote": "Let bygones be bygones.",
        "phrase": "bud",
        "clothing": "sleeveless silk dress",
        "islander": false,
        "debut": "CF",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/d/df/Pekoe_NH.png",
            "photo_url": "https://dodo.ac/np/images/4/40/Pekoe%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/c/c4/Pekoe_NH_Villager_Icon.png",
            "quote": "Let bygones be bygones.",
            "sub-personality": "B",
            "catchphrase": "bud",
            "clothing": "sleeveless silk dress",
            "clothing_variation": "Red",
            "fav_styles": [
                "Elegant",
                "Cute"
            ],
            "fav_colors": [
                "Red",
                "Beige"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/d/d3/House_of_Pekoe_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/b/b4/House_of_Pekoe_NH_Model.png",
            "house_wallpaper": "Exquisite Wall",
            "house_flooring": "Bamboo Flooring",
            "house_music": "Imperial K.K.",
            "house_music_note": ""
        },
        "appearances": [
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Purrl",
        "url": "https://nookipedia.com/wiki/Purrl",
        "alt_name": "",
        "title_color": "ffffff",
        "text_color": "848484",
        "id": "cat07",
        "image_url": "https://dodo.ac/np/images/a/a2/Purrl_NH.png",
        "species": "Cat",
        "personality": "Snooty",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "29",
        "sign": "Gemini",
        "quote": "Let them drink cream.",
        "phrase": "kitten",
        "clothing": "kung-fu tee",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/a/a2/Purrl_NH.png",
            "photo_url": "https://dodo.ac/np/images/7/70/Purrl%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/a/a0/Purrl_NH_Villager_Icon.png",
            "quote": "Let them drink cream.",
            "sub-personality": "B",
            "catchphrase": "kitten",
            "clothing": "kung-fu tee",
            "clothing_variation": "Navy blue",
            "fav_styles": [
                "Cool",
                "Elegant"
            ],
            "fav_colors": [
                "Gray",
                "Blue"
            ],
            "hobby": "Fashion",
            "house_interior_url": "https://dodo.ac/np/images/0/00/House_of_Purrl_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/8/84/House_of_Purrl_NH_Model.png",
            "house_wallpaper": "Arched-Window Wall",
            "house_flooring": "Dark Herringbone Flooring",
            "house_music": "K.K. Chorale",
            "house_music_note": ""
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WW",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Renée",
        "url": "https://nookipedia.com/wiki/Renée",
        "alt_name": "",
        "title_color": "ec7efc",
        "text_color": "fffce9",
        "id": "rhn08",
        "image_url": "https://dodo.ac/np/images/e/ef/Ren%C3%A9e_NH.png",
        "species": "Rhino",
        "personality": "Sisterly",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "28",
        "sign": "Gemini",
        "quote": "Reckless youth makes rueful age.",
        "phrase": "yo yo yo",
        "clothing": "sailor's tee",
        "islander": false,
        "debut": "NL",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/e/ef/Ren%C3%A9e_NH.png",
            "photo_url": "https://dodo.ac/np/images/d/de/Ren%C3%A9e%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/f/f6/Ren%C3%A9e_NH_Villager_Icon.png",
            "quote": "Reckless youth makes rueful age.",
            "sub-personality": "A",
            "catchphrase": "yo yo yo",
            "clothing": "sailor's tee",
            "clothing_variation": "Navy blue",
            "fav_styles": [
                "Cool",
                "Active"
            ],
            "fav_colors": [
                "Purple",
                "Yellow"
            ],
            "hobby": "Music",
            "house_interior_url": "https://dodo.ac/np/images/b/b8/House_of_Ren%C3%A9e_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/5/55/House_of_Ren%C3%A9e_NH_Model.png",
            "house_wallpaper": "Skull Wall",
            "house_flooring": "Tiger-Print Flooring",
            "house_music": "Surfin' K.K.",
            "house_music_note": ""
        },
        "appearances": [
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Roswell",
        "url": "https://nookipedia.com/wiki/Roswell",
        "alt_name": "",
        "title_color": "4c3317",
        "text_color": "fffce9",
        "id": "crd05",
        "image_url": "https://dodo.ac/np/images/d/df/Roswell_amiibo.png",
        "species": "Alligator",
        "personality": "Smug",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "2",
        "sign": "Taurus",
        "quote": "Keep your eyes on the skies.",
        "phrase": "spaaace",
        "clothing": "pineapple aloha shirt",
        "islander": true,
        "debut": "E_PLUS",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/d/df/Roswell_amiibo.png",
            "photo_url": "https://dodo.ac/np/images/3/3b/Roswell%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/7/77/Roswell_NH_Villager_Icon.png",
            "quote": "Keep your eyes on the skies.",
            "sub-personality": "A",
            "catchphrase": "spaaace",
            "clothing": "pineapple aloha shirt",
            "clothing_variation": "Blue",
            "fav_styles": [
                "Simple",
                "Elegant"
            ],
            "fav_colors": [
                "Beige",
                "Brown"
            ],
            "hobby": "Nature",
            "house_interior_url": "https://dodo.ac/np/images/5/5e/House_of_Roswell_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/a/a5/House_of_Roswell_NH_Exterior.jpg",
            "house_wallpaper": "Chain-Link Fence",
            "house_flooring": "Sandlot",
            "house_music": "K.K. Khoomei",
            "house_music_note": ""
        },
        "appearances": [
            "E_PLUS",
            "NH",
            "PC"
        ]
    },
    {
        "name": "Sasha",
        "url": "https://nookipedia.com/wiki/Sasha",
        "alt_name": "",
        "title_color": "00d1bd",
        "text_color": "fffce9",
        "id": "rbt21",
        "image_url": "https://dodo.ac/np/images/9/90/Sasha_amiibo.png",
        "species": "Rabbit",
        "personality": "Lazy",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "19",
        "sign": "Taurus",
        "quote": "Timing is everything.",
        "phrase": "hoppity",
        "clothing": "letter jacket",
        "islander": false,
        "debut": "NH",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/9/90/Sasha_amiibo.png",
            "photo_url": "https://dodo.ac/np/images/6/61/Sasha%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/8/86/Sasha_NH_Villager_Icon.png",
            "quote": "Timing is everything.",
            "sub-personality": "B",
            "catchphrase": "hoppity",
            "clothing": "letter jacket",
            "clothing_variation": "Blue",
            "fav_styles": [
                "Simple",
                "Cute"
            ],
            "fav_colors": [
                "Blue",
                "White"
            ],
            "hobby": "Fashion",
            "house_interior_url": "https://dodo.ac/np/images/c/ca/House_of_Sasha_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/5/5c/House_of_Sasha_NH_Exterior.jpg",
            "house_wallpaper": "White Painted-Wood Wall",
            "house_flooring": "Blue Dot Flooring",
            "house_music": "K.K. Lovers",
            "house_music_note": ""
        },
        "appearances": [
            "NH",
            "PC"
        ]
    },
    {
        "name": "Sylvia",
        "url": "https://nookipedia.com/wiki/Sylvia",
        "alt_name": "",
        "title_color": "a06fce",
        "text_color": "fffce9",
        "id": "kgr06",
        "image_url": "https://dodo.ac/np/images/9/93/Sylvia_NH.png",
        "species": "Kangaroo",
        "personality": "Sisterly",
        "gender": "Female",
        "birthday_month": "May",
        "birthday_day": "3",
        "sign": "Taurus",
        "quote": "Hop first; ask questions later.",
        "phrase": "boing",
        "clothing": "tropical muumuu",
        "islander": false,
        "debut": "E_PLUS",
        "prev_phrases": [
            "joey"
        ],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/9/93/Sylvia_NH.png",
            "photo_url": "https://dodo.ac/np/images/4/4c/Sylvia%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/5/52/Sylvia_NH_Villager_Icon.png",
            "quote": "Hop first; ask questions later.",
            "sub-personality": "B",
            "catchphrase": "boing",
            "clothing": "tropical muumuu",
            "clothing_variation": "Yellow",
            "fav_styles": [
                "Simple",
                "Gorgeous"
            ],
            "fav_colors": [
                "Yellow",
                "Green"
            ],
            "hobby": "Music",
            "house_interior_url": "https://dodo.ac/np/images/8/8d/House_of_Sylvia_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/7/72/House_of_Sylvia_NH_Model.png",
            "house_wallpaper": "Concrete Wall",
            "house_flooring": "Monochromatic Tile Flooring",
            "house_music": "DJ K.K.",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "E_PLUS",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "T-Bone",
        "url": "https://nookipedia.com/wiki/T-Bone",
        "alt_name": "",
        "title_color": "acc8cf",
        "text_color": "498992",
        "id": "bul05",
        "image_url": "https://dodo.ac/np/images/e/e0/T-Bone_NH.png",
        "species": "Bull",
        "personality": "Cranky",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "20",
        "sign": "Taurus",
        "quote": "Don't have a cow.",
        "phrase": "moocher",
        "clothing": "hanten jacket",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/e/e0/T-Bone_NH.png",
            "photo_url": "https://dodo.ac/np/images/b/ba/T-Bone%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/1/16/T-Bone_NH_Villager_Icon.png",
            "quote": "Don't have a cow.",
            "sub-personality": "B",
            "catchphrase": "moocher",
            "clothing": "hanten jacket",
            "clothing_variation": "Dark blue",
            "fav_styles": [
                "Cool",
                "Simple"
            ],
            "fav_colors": [
                "Blue",
                "Black"
            ],
            "hobby": "Education",
            "house_interior_url": "https://dodo.ac/np/images/7/7a/House_of_T-Bone_NH.png",
            "house_exterior_url": "https://dodo.ac/np/images/8/82/House_of_T-Bone_NH_Model.png",
            "house_wallpaper": "Arched-Window Wall",
            "house_flooring": "Arabesque Flooring",
            "house_music": "K.K. Steppe",
            "house_music_note": ""
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    },
    {
        "name": "Tank",
        "url": "https://nookipedia.com/wiki/Tank",
        "alt_name": "",
        "title_color": "8bcdea",
        "text_color": "fffad4",
        "id": "rhn00",
        "image_url": "https://dodo.ac/np/images/6/69/Tank_NH_Transparent.png",
        "species": "Rhino",
        "personality": "Jock",
        "gender": "Male",
        "birthday_month": "May",
        "birthday_day": "6",
        "sign": "Taurus",
        "quote": "Remember to keep your core muscles engaged!",
        "phrase": "kerPOW",
        "clothing": "No. 1 shirt",
        "islander": false,
        "debut": "DNM",
        "prev_phrases": [],
        "nh_details": {
            "image_url": "https://dodo.ac/np/images/d/d9/Tank_NH.png",
            "photo_url": "https://dodo.ac/np/images/5/5e/Tank%27s_Poster_NH_Texture.png",
            "icon_url": "https://dodo.ac/np/images/a/a1/Tank_NH_Villager_Icon.png",
            "quote": "Remember to keep your core muscles engaged!",
            "sub-personality": "B",
            "catchphrase": "kerPOW",
            "clothing": "No. 1 shirt",
            "clothing_variation": "",
            "fav_styles": [
                "Active",
                "Simple"
            ],
            "fav_colors": [
                "Red",
                "Green"
            ],
            "hobby": "Fitness",
            "house_interior_url": "https://dodo.ac/np/images/6/64/House_of_Tank_NH.jpg",
            "house_exterior_url": "https://dodo.ac/np/images/1/15/House_of_Tank_NH_Model.png",
            "house_wallpaper": "Bamboo-Grove Wall",
            "house_flooring": "Sandlot",
            "house_music": "K.K. Lament",
            "house_music_note": "Does not contain a stereo initially"
        },
        "appearances": [
            "DNM",
            "AC",
            "E_PLUS",
            "WW",
            "CF",
            "NL",
            "WA",
            "NH",
            "HHD",
            "PC"
        ]
    }
]"""
    }

}