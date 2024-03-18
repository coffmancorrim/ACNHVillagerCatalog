package me.proton.coffmancorrim.acnhvillagercatalog.model

import java.util.UUID

data class ListWrapper(
    var listName: String,
    val keyId: String = generateId()
){

    private companion object {
        fun generateId(): String {
            return UUID.randomUUID().toString()
        }
    }
}
