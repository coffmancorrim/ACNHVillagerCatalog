package me.proton.coffmancorrim.acnhvillagercatalog.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.proton.coffmancorrim.acnhvillagercatalog.model.NhDetails
import me.proton.coffmancorrim.acnhvillagercatalog.model.Villager

@Dao
interface VillagerDao {
    @Query("SELECT * FROM villagers WHERE favorite = 0")
    suspend fun getNonFavorites(): List<Villager>

    @Query("SELECT * FROM villagers WHERE favorite = 1")
    suspend fun getFavorites(): List<Villager>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVillager(villager: Villager)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVillagers(villagers: List<Villager>)

    @Query("SELECT COUNT(*) FROM villagers")
    suspend fun getVillagerCount(): Int

    suspend fun isDaoEmpty(): Boolean {
        return getVillagerCount() == 0
    }

    @Query("SELECT * FROM villagers WHERE name = :name")
    suspend fun getVillagerByName(name: String): Villager?

    @Update
    suspend fun updateVillager(villager: Villager)

    @Query("UPDATE villagers SET listWrapperIds = NULL")
    suspend fun removeAllListWrapperIds()
}

@Database(entities = [Villager::class], version = 1)
@TypeConverters(NhDetailsConverter::class, StringListConverter::class)
abstract class VillagerDatabase : RoomDatabase() {
    abstract fun villagerDao(): VillagerDao
}

object VillagerDatabaseSingleton {
    @Volatile
    private var INSTANCE: VillagerDatabase? = null

    fun createDatabase(context: Context){
        INSTANCE =  Room.databaseBuilder(
            context.applicationContext,
            VillagerDatabase::class.java,
            "villager_database"
        ).build()
    }

    fun getDatabase(): VillagerDatabase {
        val instance = INSTANCE
        requireNotNull(instance) { "Database not initialized. Call createDatabase() first." }
        return instance
    }


}

class StringListConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun toString(value: List<String>): String {
        return value.joinToString(",")
    }
}

object NhDetailsConverter {
    @JvmStatic
    @TypeConverter
    fun fromNhDetails(nhDetails: NhDetails): String {
        return Json.encodeToString(nhDetails)
    }

    @JvmStatic
    @TypeConverter
    fun toNhDetails(json: String): NhDetails {
        return Json.decodeFromString(json)
    }
}