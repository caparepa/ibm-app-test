package com.example.international.business.men.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.international.business.men.base.BaseUTTest
import com.example.international.business.men.base.getOrAwaitValue
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.di.configureAppTestModules
import com.example.international.business.men.repository.TransactionRepositoryImpl
import com.example.international.business.men.rules.MainCoroutineRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.mockito.Mockito.`when`
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
    fun getTransactionList() = mainCoroutineRule.runBlockingTest {
        //GIVEN
        val contextMock = mock(Context::class.java)
        val transactionItemMock = mock(TransactionItem::class.java)
        val transactionRepositoryMock = mock(TransactionRepositoryImpl::class.java)

        //instantiate the viewmodel
        val productTransactionViewModel = ProductTransactionViewModel(contextMock, mainCoroutineRule.testDispatcher)

        //Make a call to the repository and instantiate an list of mock transaction items
        `when`(transactionRepositoryMock.getTransactionList()).thenReturn(
            arrayListOf(transactionItemMock)
        )

        //THEN

        //Load task in the viewmodel
        productTransactionViewModel.getTransactionList()

        //Pause dispatcher so initial values can be verified
        mainCoroutineRule.pauseDispatcher()

        //Assert the loading indicator is shown
        productTransactionViewModel.loadingState.postValue(true)
        assertThat(productTransactionViewModel.loadingState.getOrAwaitValue(), `is`(true))

        // Execute pending coroutines actions.
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden.
        productTransactionViewModel.loadingState.postValue(false)
        assertThat(productTransactionViewModel.loadingState.getOrAwaitValue(), `is`(false))

        //then the data is post valued
        productTransactionViewModel.transactionList.postValue(arrayListOf(transactionItemMock))

        //Assert that the result from this method is a list of transactions
        assertThat(productTransactionViewModel.transactionList.getOrAwaitValue(), `is`(arrayListOf(transactionItemMock)))

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