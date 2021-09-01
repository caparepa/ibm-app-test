package com.example.international.business.men.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices =
    [Index(
        value = ["sku"],
        unique = true
    )]
)
data class ProductEntity(
    val sku: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0
}
