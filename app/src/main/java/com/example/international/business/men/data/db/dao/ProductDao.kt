package com.example.international.business.men.data.db.dao

import androidx.room.*
import com.example.international.business.men.data.db.entity.ProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun delete(id: Long)

    @Transaction
    suspend fun upsert(product: ProductEntity) {
        val id: Long = insert(product)
        if (id == -1L) {
            update(product)
        }
    }
}