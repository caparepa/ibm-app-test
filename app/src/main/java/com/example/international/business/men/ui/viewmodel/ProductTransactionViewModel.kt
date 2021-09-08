package com.example.international.business.men.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.ExtendedTransactionItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.repository.ExchangeRateRepository
import com.example.international.business.men.repository.TransactionRepository
import com.example.international.business.men.utils.CURRENCY_EUR
import com.example.international.business.men.utils.TransactionItemUtils
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProductTransactionViewModel(val context: Context) : BaseViewModel(), KoinComponent {

    private val exchangeRateRepository: ExchangeRateRepository by inject()
    private val transactionRepository: TransactionRepository by inject()
    private val transactionUtils: TransactionItemUtils by inject()

    val productList = MutableLiveData<List<TransactionItem>?>()
    val exchangeRateList = MutableLiveData<List<ExchangeRateItem>?>()
    val filteredRateList = MutableLiveData<List<ExchangeRateItem>?>()
    val transactionList = MutableLiveData<List<TransactionItem>?>()
    val transactionBySkuList = MutableLiveData<List<TransactionItem>?>()
    val totalTransactionSumInCurrency = MutableLiveData<Double?>()
    val extendedTransactionList = MutableLiveData<List<ExtendedTransactionItem>?>()

    /**
     * API Call functions
     */
    fun getTransactionList() {
        viewModelScope.launch {
            getTransactionListAsync()
        }
    }

    fun getExchangeRateList() {
        viewModelScope.launch {
            getExchangeRateListAsync()
        }
    }

    /**
     * On-the-fly data transform functions (called from the fragment observer)
     */
    fun getMissingCurrencyRates(to: String, list: List<ExchangeRateItem>) {
        val result = exchangeRateRepository.getMissingCurrencyRates(to, list)
        filteredRateList.postValue(result)
    }

    fun getProductList(list: List<TransactionItem>?) {
        val result = transactionUtils.getUniqueSkuList(list)
        productList.postValue(result)
    }

    fun getTransactionsBySku(sku: String, list: List<TransactionItem>?) {
        val result = transactionUtils.getTransactionsBySku(sku, list)
        transactionBySkuList.postValue(result)
    }

    fun getTransactionSumInCurrency(rates: List<ExchangeRateItem>, list: List<TransactionItem>) {
        val result = transactionUtils.getSkuTransactionsAmountSum(rates, list, CURRENCY_EUR)
        totalTransactionSumInCurrency.postValue(result)
    }

    fun getExtendedTransactionList(currency: String, rates: List<ExchangeRateItem>, list: List<TransactionItem>) {
        val result = transactionUtils.getExtendedTransactionList(currency, rates, list)
        extendedTransactionList.postValue(result)
    }

    /**
     * Async functions
     */
    private suspend fun getTransactionListAsync() {
        val result = kotlin.runCatching {
            showLoading()
            transactionRepository.getTransactionList()
        }
        with(result) {
            onSuccess {
                dismissLoading()
                transactionList.postValue(it)
            }
            onFailure {
                dismissLoading()
                onError.postValue(it.message)
            }
        }
    }

    private suspend fun getExchangeRateListAsync() {
        val result = kotlin.runCatching {
            showLoading()
            exchangeRateRepository.getExchangeRates()
        }
        with(result) {
            onSuccess {
                dismissLoading()
                exchangeRateList.postValue(it)
            }
            onFailure {
                dismissLoading()
                onError.postValue(it.message)
            }
        }
    }

}