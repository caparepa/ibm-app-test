package com.example.international.business.men.utils

import com.example.international.business.men.base.BaseUTTest
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.di.configureAppTestModules
import com.google.gson.reflect.TypeToken
import org.junit.Assert.*
import org.junit.Before

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class ExchangeRateItemUtilsTest : BaseUTTest(), KoinTest {

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
        val rates: MutableList<ExchangeRateItem> =
            parseArray(ratesJson, object : TypeToken<List<ExchangeRateItem>>() {}.type)

        //Assert rate list is not null nor empty
        assert(!rates.isNullOrEmpty())

        //obtain the current currency sign set
        val currencySet = rates.getCurrencySet()

        //assert ther set is not empty
        assert(currencySet.isNotEmpty())

        //obtain all currency sign permutations
        val permutations = currencySet.permutations()

        //assert the set is not empty
        assert(permutations.iterator().hasNext())

        //obtain the missing exchange rate list
        val missingRates = exchangeRateUtils.buildRatesList(permutations, currency)

        //assert the missing exchange rate list is not empty
        assert(missingRates.isNotEmpty())

        //obtain the pairs with the target currencyo sign
        val targetCurrencyPair = missingRates.filter { item -> item.to == currency }

        //assert the list is not empty
        assert(targetCurrencyPair.isNotEmpty())
    }

    @Test
    fun calculateMissingExchangeRates() {
        val ratesJson = getJson("ibm_mock_rates.json")
        val rates: List<ExchangeRateItem> =
            parseArray(ratesJson, object : TypeToken<List<ExchangeRateItem>>() {}.type)
        assertNotNull(rates)
    }
}