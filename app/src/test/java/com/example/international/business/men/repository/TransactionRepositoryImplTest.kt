package com.example.international.business.men.repository

import com.example.international.business.men.base.BaseUTTest
import com.example.international.business.men.di.configureAppTestModules
import com.example.international.business.men.network.api.ApiClient
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class TransactionRepositoryImplTest: BaseUTTest(), KoinTest {

    private lateinit var repo: TransactionRepository

    val apiClient: ApiClient by inject()
    val mockWebServer: MockWebServer by inject()

    @Before
    fun start() {
        super.setUp()
        startKoin { modules(configureAppTestModules(getMockWebServerUrl())) }
    }

    @Test
    fun getTransactionList() = runBlocking {
        mockNetworkResponseWithFileContent("ibm_mock_transactions.json", HttpURLConnection.HTTP_OK)
        repo = TransactionRepositoryImpl()
        val result = repo.getTransactionList()
        assertNotNull(result)
    }

}