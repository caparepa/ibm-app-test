package com.example.international.business.men.utils

import com.example.international.business.men.base.BaseUTTest
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.di.configureAppTestModules
import com.google.gson.reflect.TypeToken
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class ExchangeRateItemUtilsTest: BaseUTTest(), KoinTest {

    private val exchangeRateUtils: ExchangeRateItemUtils by inject()
    private val currency = CURRENCY_EUR

    @Before
    fun start() {
        super.setUp()
        startKoin { modules(configureAppTestModules(getMockWebServerUrl())) }
    }

    @Test
    fun obtainMissingExchangePairs() {
        val ratesJson = getJson("ibm_mock_rates.json")
        val rates: List<ExchangeRateItem> =
            parseArray(ratesJson, object : TypeToken<List<ExchangeRateItem>>() {}.type)
        assertNotNull(rates)
    }

    @Test
    fun calculateMissingExchangeRates() {
        val ratesJson = getJson("ibm_mock_rates.json")
        val rates: List<ExchangeRateItem> =
            parseArray(ratesJson, object : TypeToken<List<ExchangeRateItem>>() {}.type)
        assertNotNull(rates)
    }
}