package me.proton.coffmancorrim.acnhvillagercatalog.model

import androidx.room.Entity
import androidx.room.PrimaryKey
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

