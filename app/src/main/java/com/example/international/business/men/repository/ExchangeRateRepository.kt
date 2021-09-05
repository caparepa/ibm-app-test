package com.example.international.business.men.repository

import com.example.international.business.men.data.model.ExchangeRateItem

interface ExchangeRateRepository {

    suspend fun getExchangeRates(): List<ExchangeRateItem>?
    fun getMissingCurrencyRates(currencyTo: String, list: List<ExchangeRateItem>): List<ExchangeRateItem>

}