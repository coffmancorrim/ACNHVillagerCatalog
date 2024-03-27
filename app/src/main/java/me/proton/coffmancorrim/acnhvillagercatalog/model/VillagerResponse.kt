package me.proton.coffmancorrim.acnhvillagercatalog.model

sealed class VillagerResponse {
    data class Success(val villagerList: List<Villager>) : VillagerResponse()
    object Error : VillagerResponse()
}

