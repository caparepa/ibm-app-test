package com.example.international.business.men.repository

import com.example.international.business.men.data.db.dao.ExchangeRateDao
import com.example.international.business.men.data.db.entity.ExchangeRateEntity
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.network.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExchangeRateRepositoryImpl(val exchangeRateDao: ExchangeRateDao) : ExchangeRateRepository, KoinComponent {

    private val api = ApiClient.invoke()

    override suspend fun getExchangeRates(): List<ExchangeRateItem>? = withContext(Dispatchers.IO){
        val response = api.getExchangeRates()
        val list = response.body()
        persistExchangeRate(list)
        list
    }

    override suspend fun persistExchangeRate(list: List<ExchangeRateItem>?): Unit = withContext(Dispatchers.IO) {
        list?.forEach { item ->
            val entity = ExchangeRateEntity(
                from = item.from,
                to = item.to,
                rate = item.rate
            )
            exchangeRateDao.upsert(entity)
        }
    }

    override suspend fun fetchExchangeRates(): List<ExchangeRateEntity> = withContext(Dispatchers.IO) {
        exchangeRateDao.getAllRates()
    }

    override suspend fun fetchEuroRate(currency: String): ExchangeRateEntity? = withContext(Dispatchers.IO) {
        exchangeRateDao.getEuroRateByCurrency(currency)
    }
}