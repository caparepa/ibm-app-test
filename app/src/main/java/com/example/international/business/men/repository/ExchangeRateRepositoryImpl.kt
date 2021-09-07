package com.example.international.business.men.repository

import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.network.api.ApiClient
import com.example.international.business.men.utils.ExchangeRateItemUtils
import com.example.international.business.men.utils.getCurrencySet
import com.example.international.business.men.utils.permutations
import com.example.international.business.men.utils.roundToHalfEven
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExchangeRateRepositoryImpl() : ExchangeRateRepository, KoinComponent {

    private val api = ApiClient.invoke()
    private val exchangeRateUtils: ExchangeRateItemUtils by inject()

    override suspend fun getExchangeRates(): List<ExchangeRateItem>? = withContext(Dispatchers.IO){
        val response = api.getExchangeRates()
        val list = response.body()
        list
    }

    /**
     * Get the missing rates (functional)
     */
    override fun getMissingCurrencyRates(currencyTo: String, list: List<ExchangeRateItem>): List<ExchangeRateItem> {
        var result: List<ExchangeRateItem> = arrayListOf<ExchangeRateItem>()

        //get missing exchange rate pairs
        val modList = exchangeRateUtils.obtainMissingExchangePairs(list, currencyTo)

        //calculate the missing (null) rates
        result = exchangeRateUtils.calculateMissingExchangeRates(currencyTo, modList)

        //return result
        return result
    }
}