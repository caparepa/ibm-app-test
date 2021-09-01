package com.example.international.business.men.repository

import com.example.international.business.men.data.db.entity.ProductEntity
import com.example.international.business.men.data.model.TransactionItem

interface TransactionRepository {

    suspend fun getTransactionList(): List<TransactionItem>?
    fun getTransactionsBySku(sku: String, list: List<TransactionItem>?): List<TransactionItem>?
    fun getSkuTransactionsAmountSum(list: List<TransactionItem>?): Double
    fun convertAmount(amount: Double, currency: String): Double

    suspend fun persistProductList(list: List<TransactionItem>?)
    suspend fun fetchProductList(): List<ProductEntity>

}