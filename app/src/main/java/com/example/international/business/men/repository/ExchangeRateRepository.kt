package com.example.international.business.men.repository

import com.example.international.business.men.data.db.entity.ExchangeRateEntity
import com.example.international.business.men.data.model.ExchangeRateItem

interface ExchangeRateRepository {

    suspend fun getExchangeRates(): List<ExchangeRateItem>?
    suspend fun persistExchangeRate(list: List<ExchangeRateItem>?)

    suspend fun fetchExchangeRates(): List<ExchangeRateEntity>

}