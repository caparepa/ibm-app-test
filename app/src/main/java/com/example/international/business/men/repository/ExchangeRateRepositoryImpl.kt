package com.example.international.business.men.repository

import com.example.international.business.men.data.db.dao.ExchangeRateDao
import com.example.international.business.men.data.db.entity.ExchangeRateEntity
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.network.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExchangeRateRepositoryImpl() : ExchangeRateRepository, KoinComponent {

    private val api = ApiClient.invoke()

    override suspend fun getExchangeRates(): List<ExchangeRateItem>? = withContext(Dispatchers.IO){
        val response = api.getExchangeRates()
        val list = response.body()
        getOtherList(list)
        list
    }

    override fun getComplementaryExchangeRate(
        from: String,
        list: List<ExchangeRateItem>?
    ): ExchangeRateItem? {
        TODO("Not yet implemented")
    }

    private fun getOtherList(list: List<ExchangeRateItem>?) {
        var editList = list?.toMutableList()
        var newList = arrayListOf<ExchangeRateItem>()
        list?.forEach { og ->
            editList?.forEach { ed ->

            }
        }
    }
}