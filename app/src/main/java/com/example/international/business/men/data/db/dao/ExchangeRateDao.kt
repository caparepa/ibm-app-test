package com.example.international.business.men.data.db.dao

import androidx.room.*
import com.example.international.business.men.data.db.entity.ExchangeRateEntity

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(rate: ExchangeRateEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(rate: ExchangeRateEntity)

    @Query("DELETE FROM exchange_rates WHERE id = :id")
    suspend fun delete(id: Long)

    @Transaction
    suspend fun upsert(rate: ExchangeRateEntity) {
        val id: Long = insert(rate)
        if (id == -1L) {
            update(rate)
        }
    }

    @Query("SELECT * FROM exchange_rates WHERE `from` = :currency AND `to` = \"EUR\"")
    suspend fun getEuroRateByCurrency(currency: String?): ExchangeRateEntity

    @Query("SELECT * FROM exchange_rates")
    suspend fun getAllRates(): List<ExchangeRateEntity>
}