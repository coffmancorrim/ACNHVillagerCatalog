package me.proton.coffmancorrim.acnhvillagercatalog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.proton.coffmancorrim.acnhvillagercatalog.data.AnimalCrossingRepository
import me.proton.coffmancorrim.acnhvillagercatalog.data.AnimalCrossingRepositoryImpl
import me.proton.coffmancorrim.acnhvillagercatalog.data.VillagerDatabaseSingleton
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper
import me.proton.coffmancorrim.acnhvillagercatalog.data.ListWrapperDatabase
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import me.proton.coffmancorrim.acnhvillagercatalog.model.VillagerResponse

class MainViewModel(
    private val animalCrossingRepository: AnimalCrossingRepository
    ) : ViewModel() {
    var customKey: String? = null
    var detailVillager: Villager? = null
    var reloadVillagerData: Boolean = true

    private val _mutableVillagerList: MutableStateFlow<VillagerEvent> =
        MutableStateFlow(VillagerEvent.Loading)
    val villagerList: StateFlow<VillagerEvent>
        get() = _mutableVillagerList

    private val _listOfNames: MutableStateFlow<MutableList<ListWrapper>> =
        MutableStateFlow(mutableListOf<ListWrapper>())
    val listOfNames: StateFlow<List<ListWrapper>>
        get() = _listOfNames

    private val _dictOfVillagerLists: MutableStateFlow<MutableMap<String, MutableList<Villager>>> =
        MutableStateFlow(mutableMapOf())
    val dictOfVillagerLists: StateFlow<Map<String, List<Villager>>>
        get() = _dictOfVillagerLists

    private val _mutableFavoritesList = MutableStateFlow(mutableListOf<Villager>())
    val favoritesList: StateFlow<List<Villager>>
        get() = _mutableFavoritesList

    private val _isFavoritesList = MutableStateFlow<Boolean>(false)
    val isFavoritesList: StateFlow<Boolean>
        get() = _isFavoritesList

    private val _isListClickable = MutableStateFlow<Boolean>(true)
    val isListClickable: StateFlow<Boolean>
        get() = _isListClickable

    fun fillVillagerData() {
        viewModelScope.launch {
            if(villagerList.value is VillagerEvent.Error){
                _mutableVillagerList.value = VillagerEvent.Loading
            }

            if (villagerList.value is VillagerEvent.Success){
                Log.d("FILL_VILLAGER_DATA()", "VILLAGER DATA FOUND IN MEMORY")
            } else if (animalCrossingRepository.getVillagersFromDao() is VillagerResponse.Success){
                Log.d("FILL_VILLAGER_DATA()", "ATTEMPTING TO RETRIEVE DATA FROM LOCAL DATABASE")

                if (animalCrossingRepository.getListWrapperDao() != null){
                    _listOfNames.value = animalCrossingRepository.getListWrapperDao() as MutableList<ListWrapper>
                }

                val localResponse = animalCrossingRepository.getVillagersFromDao() as VillagerResponse.Success
                _mutableVillagerList.value = VillagerEvent.Success(
                    updateVillagersInMapWithListWrappersAndClear(
                        localResponse.villagerList,
                        _listOfNames.value,
                        _dictOfVillagerLists.value
                    )
                )

                val localFavoritesResponse = animalCrossingRepository.getFavoriteVillagersFromDao()
                if (localFavoritesResponse is VillagerResponse.Success){
                    _mutableFavoritesList.value = updateVillagersInMapWithListWrappersAndClear(
                        localFavoritesResponse.villagerList,
                        _listOfNames.value,
                        _dictOfVillagerLists.value
                    ) as MutableList<Villager>
                }
                //purge the database of any VillagerWrapperIds, at the end of the apps lifecycle we create and add these values to the database
                //we do not want any conflicts of Villagers in custom lists that do not exist, are not updated
                animalCrossingRepository.cleanseVillagerWrapperIds()
            } else {
                Log.d("FILL_VILLAGER_DATA()", "ATTEMPTING TO RETRIEVE DATA FROM API")
                when (val response = animalCrossingRepository.getVillagers()) {
                    is VillagerResponse.Success -> {
                        _mutableVillagerList.value = VillagerEvent.Success(response.villagerList)
                        updateDao()
                    }

                    VillagerResponse.Error -> _mutableVillagerList.value = VillagerEvent.Error
                }
            }
        }
    }

    private fun updateVillagersInMapWithListWrappersAndClear(
        villagerList: List<Villager>,
        listWrapperList: List<ListWrapper>,
        villagerMap: MutableMap<String, MutableList<Villager>>
    ): List<Villager> {
        for (villager in villagerList) {
            villager.listWrapperIds?.let { listWrapperIds ->
                Log.d("UPDATE_MAP_LIST_WRAPPERS", "Processing villager: ${villager.name}, with listWrapperIds: ${villager.listWrapperIds.toString()}")
                for (listWrapper in listWrapperList) {
                    if (listWrapper.keyId in listWrapperIds) {
                        Log.d("UPDATE_MAP_LIST_WRAPPERS", "Adding villager to list with keyId: ${listWrapper.keyId}, name: ${listWrapper.listName}")
                        val villagerListForKeyId = villagerMap.getOrPut(listWrapper.keyId) { mutableListOf() }
                        villagerListForKeyId.add(villager)
                        Log.d("UPDATE_MAP_LIST_WRAPPERS", "Villager added to list: $villagerListForKeyId")
                    }
                }
                // Clear the listWrapperIds after adding the villager to the map
                // we do this because we have a function to add all the Id data at the end of the activity life cycle
                villager.listWrapperIds = null
                Log.d("UPDATE_MAP_LIST_WRAPPERS", "Cleared listWrapperIds for villager: $villager")
            }
        }

        return villagerList
    }

    suspend fun updateDao() {
        Log.d("UpdateDao", "Updating DAO")

        if (_mutableVillagerList.value is VillagerEvent.Success) {
            val villagers = (_mutableVillagerList.value as VillagerEvent.Success).villagersList
            Log.d("UpdateDao", "Adding villagers to DAO: $villagers")
            animalCrossingRepository.addVillagersToDao(villagers)
        }

        Log.d("UpdateDao", "Adding favorites to DAO: ${favoritesList.value}")
        animalCrossingRepository.addVillagersToDao(favoritesList.value)

        Log.d("UpdateDao", "Adding list wrappers to DAO: ${_listOfNames.value}")
        animalCrossingRepository.addListWrappersToDao(_listOfNames.value)

        Log.d("UpdateDao", "Processing villagers by list wrapper")
        processVillagersByListWrapper(listOfNames.value, dictOfVillagerLists.value)
    }

    private suspend fun processVillagersByListWrapper(listWrapperList: List<ListWrapper>, villagerMap: Map<String, List<Villager>>) {
        for (listWrapper in listWrapperList) {
            val keyId = listWrapper.keyId
            val villagerList = villagerMap[keyId]
            villagerList?.let { villagers ->
                for (villager in villagers) {
                    Log.d("processVillagersByListWrapper", "$villager.name, $keyId")
                    animalCrossingRepository.addListWrapperIdToVillager(villager.name, keyId)
                }
            }
        }
    }

    fun isVillagerInList(itemToCheck: Villager, villagerList: List<Villager>): Boolean {
        return itemToCheck in villagerList
    }

    fun addCustomListHelper() {
        val listNameWrapper = ListWrapper("NewList")
        addCustomList(listNameWrapper)
    }

    private fun addCustomList(listNameWrapper: ListWrapper) {
        _listOfNames.value.add(listNameWrapper)
        val newDictionary = _dictOfVillagerLists.value.toMutableMap()
            .apply { put(listNameWrapper.keyId, mutableListOf()) }
        _dictOfVillagerLists.value = newDictionary
    }

    fun removeCustomList(listNameWrapper: ListWrapper) {
        _listOfNames.value.remove(listNameWrapper)
        val newDictionary =
            _dictOfVillagerLists.value.toMutableMap().apply { remove(listNameWrapper.keyId) }
        _dictOfVillagerLists.value = newDictionary
    }

    fun renameListWrapper(newName: String, index: Int) {
        _listOfNames.value[index].listName = newName
    }

    fun addVillagerToCustomList(name: String, villager: Villager) {
        val list = _dictOfVillagerLists.value[name]?.toMutableList() ?: mutableListOf()
        val newDictionary =
            _dictOfVillagerLists.value.toMutableMap().apply { put(name,
                (list + villager) as MutableList<Villager>
            ) }
        _dictOfVillagerLists.value = newDictionary
    }

    fun removeVillagerFromCustomList(name: String, villager: Villager) {
        val list = _dictOfVillagerLists.value[name]?.toMutableList() ?: return
        val newDictionary =
            _dictOfVillagerLists.value.toMutableMap().apply { put(name,
                (list - villager) as MutableList<Villager>
            ) }
        _dictOfVillagerLists.value = newDictionary
    }

    fun addFavoriteVillager(villager: Villager) {
        _mutableFavoritesList.value.add(villager)
    }

    fun removeFavoriteVillager(villager: Villager) {
        _mutableFavoritesList.value.remove(villager)
    }

    fun setFavorite(villagerId: String, favorite: Boolean) {
        for (villager in _mutableFavoritesList.value){
            if (villagerId == villager.id) {
                villager.favorite = favorite
            }
        }
    }

    fun <T> filterListForFavorites(inputList: List<T>, favoritesList: List<T>): List<T> {
        val filteredList = mutableListOf<T>()

        for (item in inputList) {
            if (item !in favoritesList) {
                filteredList.add(item)
            }
        }

        return filteredList
    }

    fun toggleIsListClickableBoolean() {
        _isListClickable.value = !(_isListClickable.value)
        Log.d("IS_CLICKABLE", "TOGGLED IS_LIST_CLICKABLE: " + _isListClickable.value.toString())
    }

    fun toggleReloadVillagerData() {
        reloadVillagerData = !(reloadVillagerData)
        Log.d("RELOAD", "toggleReloadVillagerData(): " + reloadVillagerData.toString())
    }

    fun toggleFavoritesBoolean() {
        _isFavoritesList.value = !(_isFavoritesList.value)
    }

    sealed class VillagerEvent {
        data class Success(val villagersList: List<Villager>) : VillagerEvent()
        object Error : VillagerEvent()
        object Loading : VillagerEvent()
    }
}

class MainViewModelFactory(private val repository: AnimalCrossingRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}
