package com.example.international.business.men.repository

import com.example.international.business.men.data.db.entity.ProductEntity
import com.example.international.business.men.data.model.TransactionItem

interface TransactionRepository {

    suspend fun getTransactionList(): List<TransactionItem>?
    suspend fun getTransactionsBySku(sku: String): List<TransactionItem>?
    suspend fun getTransactionAmountSum(list: List<TransactionItem>?): Double

    suspend fun persistProductList(list: List<TransactionItem>?)
    suspend fun fetchProductList(): List<ProductEntity>

}