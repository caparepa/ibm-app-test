package com.example.international.business.men.repository

import com.example.international.business.men.base.BaseUTTest
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.di.configureAppTestModules
import com.example.international.business.men.network.api.ApiClient
import com.example.international.business.men.utils.*
import com.google.gson.reflect.TypeToken
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class ExchangeRateRepositoryImplTest : BaseUTTest(), KoinTest {

    private lateinit var repo: ExchangeRateRepository

    val api: ApiClient by inject()
    val mockWebServer: MockWebServer by inject()

    private val exchangeRateUtils: ExchangeRateItemUtils by inject()
    private val currency = CURRENCY_EUR

    @Before
    fun start() {
        super.setUp()
        startKoin { modules(configureAppTestModules(getMockWebServerUrl())) }
    }

    @Test
    fun getExchangeRates() = runBlocking {
        mockNetworkResponseWithFileContent("ibm_mock_rates.json", HttpURLConnection.HTTP_OK)
        repo = ExchangeRateRepositoryImpl()
        val result = repo.getExchangeRates()
        assertNotNull(result)
    }

    @Test
    fun getMissingCurrencyRates() {
        val dataJson = getJson("ibm_mock_rates.json")
        val rates: List<ExchangeRateItem> =
            parseArray(dataJson, object : TypeToken<List<ExchangeRateItem>>() {}.type)

        val modList = exchangeRateUtils.obtainMissingExchangePairs(rates, currency)
        assert(modList.isNotEmpty())

        val result = exchangeRateUtils.calculateMissingExchangeRates(currency, modList)
        assert(result.isNotEmpty())
    }
}