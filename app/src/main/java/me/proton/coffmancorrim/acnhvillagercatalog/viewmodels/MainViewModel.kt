package me.proton.coffmancorrim.acnhvillagercatalog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.proton.coffmancorrim.acnhvillagercatalog.model.Birthday
import me.proton.coffmancorrim.acnhvillagercatalog.model.Gender
import me.proton.coffmancorrim.acnhvillagercatalog.model.Hobby
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper
import me.proton.coffmancorrim.acnhvillagercatalog.model.NhDetails
import me.proton.coffmancorrim.acnhvillagercatalog.model.Personality
import me.proton.coffmancorrim.acnhvillagercatalog.model.Species
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import kotlin.random.Random

class MainViewModel : ViewModel() {
    var customKey: String? = null
    var detailVillager: Villager? = null

    private val _isListClickable = MutableStateFlow<Boolean>(true)
    val isListClickable: StateFlow<Boolean>
        get() = _isListClickable

    fun toggleIsListClickableBoolean() {
        _isListClickable.value = !(_isListClickable.value)
        Log.d("IS_CLICKABLE", "TOGGLED IS_LIST_CLICKABLE: " + _isListClickable.value.toString())
    }


    private val _mutableVillagerList = MutableStateFlow(fillVillagerData())
    val villagerList: StateFlow<List<Villager>>
        get() = _mutableVillagerList

    private val _mutableVillagerListFiltered = MutableStateFlow(villagerList.value.toMutableList())
    val villagerListFiltered: StateFlow<List<Villager>>
        get() = _mutableVillagerListFiltered

    private val _listOfNames: MutableStateFlow<MutableList<ListWrapper>> = MutableStateFlow(mutableListOf<ListWrapper>())
    val listOfNames: StateFlow<List<ListWrapper>>
        get() = _listOfNames



    private val _dictOfVillagerLists: MutableStateFlow<Map<String, List<Villager>>> = MutableStateFlow(emptyMap())
    val dictOfVillagerLists: StateFlow<Map<String, List<Villager>>>
        get() = _dictOfVillagerLists

    fun addCustomListHelper(){
        val listNameWrapper = ListWrapper("NewList")
        addCustomList(listNameWrapper)
    }

    private fun addCustomList(listNameWrapper: ListWrapper) {
        _listOfNames.value.add(listNameWrapper)
        val newDictionary = _dictOfVillagerLists.value.toMutableMap().apply { put(listNameWrapper.keyId, emptyList()) }
        _dictOfVillagerLists.value = newDictionary
    }

    fun removeCustomList(listNameWrapper: ListWrapper) {
        _listOfNames.value.remove(listNameWrapper)
        val newDictionary = _dictOfVillagerLists.value.toMutableMap().apply { remove(listNameWrapper.keyId) }
        _dictOfVillagerLists.value = newDictionary
    }

    fun renameListWrapper(newName: String, index: Int){
        _listOfNames.value[index].listName = newName
    }

    fun addVillagerToCustomList(name: String, villager: Villager) {
        val list = _dictOfVillagerLists.value[name]?.toMutableList() ?: mutableListOf()
        val newDictionary = _dictOfVillagerLists.value.toMutableMap().apply { put(name, list + villager) }
        _dictOfVillagerLists.value = newDictionary
    }

    fun removeVillagerFromCustomList(name: String, villager: Villager) {
        val list = _dictOfVillagerLists.value[name]?.toMutableList() ?: return
        val newDictionary = _dictOfVillagerLists.value.toMutableMap().apply { put(name, list - villager) }
        _dictOfVillagerLists.value = newDictionary
    }

    private val _mutableFavoritesList = MutableStateFlow(mutableListOf<Villager>(generateDummyVillager()))
    val favoritesList : StateFlow<List<Villager>>
        get() = _mutableFavoritesList

    private val _isFavoritesList = MutableStateFlow<Boolean>(false)
    val isFavoritesList: StateFlow<Boolean>
        get() = _isFavoritesList

    fun toggleFavoritesBoolean() {
        _isFavoritesList.value = !(_isFavoritesList.value)
    }


    fun addFavoriteVillager(villager: Villager){
        _mutableFavoritesList.value.add(villager)
    }

    fun removeFavoriteVillager(villager: Villager){
        _mutableFavoritesList.value.remove(villager)
    }

    fun isVillagerInList(itemToCheck: Villager, villagerList: List<Villager>): Boolean {
        return itemToCheck in villagerList
    }


    private fun fillVillagerData() : List<Villager>{
        //TODO: GET DATA FROM API, FOR NOW DATA IS GENERATED WITH DUMMY VALUES FROM TEMP FUNCTION
        return generateVillagerList(20)
    }

    fun updateFilteredList() {
        val iterator = _mutableVillagerListFiltered.value.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item in favoritesList.value) {
                iterator.remove()
            }
        }

    }


    fun <T> copyRandomElements(inputList: List<T>, numElements: Int): List<T> {
        if (numElements <= 0 || numElements > inputList.size) {
            throw IllegalArgumentException("Number of elements to copy must be greater than 0 and less than or equal to the size of the input list.")
        }

        val shuffledList = inputList.shuffled()
        return shuffledList.subList(0, numElements)
    }






    companion object{//TODO: DUMMY GENERATED DATA GET RID OF AT SOME POINT????
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
                birthday = Birthday(months.random(), days.random()),
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
    }
}