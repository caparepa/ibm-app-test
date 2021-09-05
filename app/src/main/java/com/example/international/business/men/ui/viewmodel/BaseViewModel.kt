package com.example.international.business.men.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel: ViewModel() {
    val loadingState = MutableLiveData<Boolean>()
    val onSuccess = MutableLiveData<Any>()
    val onError = MutableLiveData<String>()
    val onConnError = MutableLiveData<String>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    protected fun showLoading() = loadingState.postValue(true)

    protected fun dismissLoading() = loadingState.postValue(false)
}