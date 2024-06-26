package me.proton.coffmancorrim.acnhvillagercatalog.data

import kotlinx.serialization.json.Json
import me.proton.coffmancorrim.acnhvillagercatalog.BuildConfig
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


interface AnimalCrossingApi {
    @Headers("X-API-KEY: ${BuildConfig.API_KEY}")
    @GET("villagers?nhdetails=true&game=NH")
    suspend fun getVillagers() : Response<List<Villager>>
}

