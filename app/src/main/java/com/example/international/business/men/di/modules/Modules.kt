package com.example.international.business.men.di.modules

import android.content.Context
import com.example.international.business.men.network.api.ApiClient
import com.example.international.business.men.network.interceptor.ConnectivityInterceptor
import com.example.international.business.men.network.interceptor.ConnectivityInterceptorImpl
import com.example.international.business.men.repository.ExchangeRateRepository
import com.example.international.business.men.repository.ExchangeRateRepositoryImpl
import com.example.international.business.men.repository.TransactionRepository
import com.example.international.business.men.repository.TransactionRepositoryImpl
import com.example.international.business.men.ui.viewmodel.ProductTransactionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient.invoke() }
    single<ConnectivityInterceptor> { ConnectivityInterceptorImpl(get()) }
}

val repositoryModule = module {
    factory<TransactionRepository> { TransactionRepositoryImpl() }
    factory<ExchangeRateRepository> { ExchangeRateRepositoryImpl() }
}

val dataModule = module {
    single { androidContext().getSharedPreferences("Prefs", Context.MODE_PRIVATE) }
}

val viewModelModule = module {
    viewModel { ProductTransactionViewModel(get()) }
}
