package com.example.international.business.men.repository

import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.ExtendedTransactionItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.network.api.ApiClient
import com.example.international.business.men.utils.roundToHalfEven
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

}