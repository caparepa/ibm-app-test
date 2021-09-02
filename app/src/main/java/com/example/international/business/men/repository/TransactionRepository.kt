package com.example.international.business.men.repository

import com.example.international.business.men.data.model.TransactionItem

interface TransactionRepository {

    suspend fun getTransactionList(): List<TransactionItem>?
    fun getUniqueSkuList(list: List<TransactionItem>?): List<TransactionItem>?
    fun getTransactionsBySku(sku: String, list: List<TransactionItem>?): List<TransactionItem>?
    fun getSkuTransactionsAmountSum(list: List<TransactionItem>?): Double
    fun convertAmount(amount: Double, currency: String): Double
}