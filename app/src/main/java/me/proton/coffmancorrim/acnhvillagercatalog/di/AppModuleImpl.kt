package me.proton.coffmancorrim.acnhvillagercatalog.di

import android.content.Context
import androidx.room.Room
import kotlinx.serialization.json.Json
import me.proton.coffmancorrim.acnhvillagercatalog.data.AnimalCrossingApi
import me.proton.coffmancorrim.acnhvillagercatalog.data.AnimalCrossingRepository
import me.proton.coffmancorrim.acnhvillagercatalog.data.AnimalCrossingRepositoryImpl
import me.proton.coffmancorrim.acnhvillagercatalog.data.ListWrapperDatabase
import me.proton.coffmancorrim.acnhvillagercatalog.data.VillagerDatabase
import me.proton.coffmancorrim.acnhvillagercatalog.data.VillagerDatabaseSingleton
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppModuleImpl(context: Context) : AppModule{
    private val BASE_URL = "https://api.nookipedia.com/"
    val json = Json { ignoreUnknownKeys = true }

    override val animalCrossingApi: AnimalCrossingApi by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
            .create(AnimalCrossingApi::class.java)
    }

    override val animalcrossingDatabase: VillagerDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            VillagerDatabase::class.java,
            "villager_database"
        ).build()
    }

    override val listWrapperDatabase: ListWrapperDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            ListWrapperDatabase::class.java,
            "list_wrapper_database"
        ).build()
    }

    override val animalCrossingRepository: AnimalCrossingRepository by lazy {
        AnimalCrossingRepositoryImpl(animalCrossingApi, animalcrossingDatabase.villagerDao(), listWrapperDatabase.listWrapperDao())
    }
}