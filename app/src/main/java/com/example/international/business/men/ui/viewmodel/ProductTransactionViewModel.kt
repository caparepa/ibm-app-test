package com.example.international.business.men.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.repository.ExchangeRateRepository
import com.example.international.business.men.repository.TransactionRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProductTransactionViewModel(val context: Context): BaseViewModel(), KoinComponent {

    private val exchangeRateRepository: ExchangeRateRepository by inject()
    private val transactionRepository: TransactionRepository by inject()

    val productList = MutableLiveData<List<String>?>()
    val exchangeRateList = MutableLiveData<List<ExchangeRateItem>?>()
    val transactionList = MutableLiveData<List<TransactionItem>?>()

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

    private suspend fun getTransactionListAsync() {
        val result = kotlin.runCatching {
            transactionRepository.getTransactionList()
        }
        with(result) {
            onSuccess {
                showLoading()
                transactionList.postValue(it)
            }
            onFailure {
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
                exchangeRateList.postValue(it)
            }
            onFailure {
                onError.postValue(it.message)
            }
        }
    }

}