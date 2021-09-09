package com.example.international.business.men.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.international.business.men.base.BaseUTTest
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.di.configureAppTestModules
import com.example.international.business.men.repository.TransactionRepositoryImpl
import com.example.international.business.men.rules.MainCoroutineRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class ProductTransactionViewModelTest: BaseUTTest(), KoinTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun start() {
        super.setUp()
        startKoin { modules(configureAppTestModules(getMockWebServerUrl())) }
    }


    @Test
    fun getTransactionList() {
        mainCoroutineRule.runBlockingTest {
            //Given
            val transactionItemMock = mock(TransactionItem::class.java)
            val transactionRepositoryMock = mock(TransactionRepositoryImpl::class.java)

            whenever(transactionRepositoryMock.getTransactionList()).thenReturn(
                arrayListOf(transactionItemMock)
            )

        }
    }

    @Test
    fun getExchangeRateList() {
    }

    @Test
    fun getMissingCurrencyRates() {
    }

    @Test
    fun getProductList() {
    }

    @Test
    fun getTransactionsBySku() {
    }

    @Test
    fun getTransactionSumInCurrency() {
    }

    @Test
    fun getExtendedTransactionList() {
    }
}