package com.example.international.business.men.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exchange_rates"
)
data class ExchangeRateEntity(
    val from: String?,
    val to: String?,
    val rate: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0
}
