package com.example.international.business.men.repository

import com.example.international.business.men.data.db.dao.ProductDao
import com.example.international.business.men.data.db.entity.ProductEntity
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.network.api.ApiClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TransactionRepositoryImpl(val productDao: ProductDao) : TransactionRepository, KoinComponent {

    private val api: ApiClient by inject()

    override suspend fun getTransactionList(): List<TransactionItem>? {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionsBySku(sku: String): List<TransactionItem>? {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionAmountSum(list: List<TransactionItem>?): Double {
        TODO("Not yet implemented")
    }

    override suspend fun persistProductList(list: List<TransactionItem>?) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchProductList(): List<ProductEntity> {
        TODO("Not yet implemented")
    }
}