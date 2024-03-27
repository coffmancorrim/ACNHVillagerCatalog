package me.proton.coffmancorrim.acnhvillagercatalog.model

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import me.proton.coffmancorrim.acnhvillagercatalog.data.VillagerDatabase
import me.proton.coffmancorrim.acnhvillagercatalog.data.VillagerDatabaseSingleton
import java.util.UUID

@Entity(tableName = "ListNames")
data class ListWrapper(
    var listName: String,
    @PrimaryKey
    val keyId: String = generateId()
) {
    companion object {
        fun generateId(): String {
            return UUID.randomUUID().toString()
        }
    }
}

@Dao
interface ListWrapperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<ListWrapper>)

    @Query("SELECT * FROM ListNames")
    suspend fun getAllLists(): List<ListWrapper>

    @Query("SELECT COUNT(*) FROM ListNames")
    suspend fun getListNamesCount(): Int

    suspend fun isDaoEmpty(): Boolean {
        return getListNamesCount() == 0
    }
}

@Database(entities = [ListWrapper::class], version = 1)
abstract class ListWrapperDatabase : RoomDatabase() {
    abstract fun listWrapperDao(): ListWrapperDao

    companion object {
        @Volatile
        private var INSTANCE: ListWrapperDatabase? = null

        fun createDatabase(context: Context){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ListWrapperDatabase::class.java,
                "list_wrapper_database"
            ).build()
        }

        fun getDatabase(): ListWrapperDatabase {
            val instance = INSTANCE
            requireNotNull(instance) { "Database not initialized. Call createDatabase() first." }
            return instance
        }
    }
}