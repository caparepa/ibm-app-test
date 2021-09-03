package com.example.international.business.men.repository

import com.example.international.business.men.data.db.dao.ProductDao
import com.example.international.business.men.data.db.entity.ProductEntity
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.network.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class TransactionRepositoryImpl() : TransactionRepository, KoinComponent {

    private val api = ApiClient.invoke()

    override suspend fun getTransactionList(): List<TransactionItem>? = withContext(Dispatchers.IO) {
        val response = api.getTransactionList()
        val list = response.body()
        list
    }

    override fun getUniqueSkuList(list: List<TransactionItem>?): List<TransactionItem>? {
        return list?.distinctBy { item -> item.sku }
    }

    override fun getTransactionsBySku(sku: String, list: List<TransactionItem>?): List<TransactionItem>? {
        return list?.filter { item -> item.sku == sku}
    }

    override fun getSkuTransactionsAmountSum(rates: List<ExchangeRateItem>, list: List<TransactionItem>?): Double {
        return 0.0
    }

    override fun convertAmount(amount: Double, currency: String): Double {
        TODO("Not yet implemented")
    }
}