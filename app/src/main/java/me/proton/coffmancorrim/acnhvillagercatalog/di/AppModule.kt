package me.proton.coffmancorrim.acnhvillagercatalog.di

import me.proton.coffmancorrim.acnhvillagercatalog.data.AnimalCrossingApi
import me.proton.coffmancorrim.acnhvillagercatalog.data.AnimalCrossingRepository
import me.proton.coffmancorrim.acnhvillagercatalog.data.ListWrapperDatabase
import me.proton.coffmancorrim.acnhvillagercatalog.data.VillagerDatabase

interface AppModule {
    val animalCrossingApi: AnimalCrossingApi
    val animalCrossingRepository: AnimalCrossingRepository
    val animalcrossingDatabase: VillagerDatabase
    val listWrapperDatabase: ListWrapperDatabase
}