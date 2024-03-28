package me.proton.coffmancorrim.acnhvillagercatalog.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import me.proton.coffmancorrim.acnhvillagercatalog.model.ListWrapper

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


}