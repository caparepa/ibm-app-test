package com.example.international.business.men.di.modules

import android.content.Context
import com.example.international.business.men.data.InternationalBusinessMenDatabase
import com.example.international.business.men.data.db.dao.ProductDao
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

val databaseModule = module {
    /*single { InternationalBusinessMenDatabase.invoke(androidContext()) }

    fun provideProductDao(database: InternationalBusinessMenDatabase): ProductDao {
        return database.getProductDao()
    }

    single { provideProductDao(get()) }*/

    //TODO: YOUR CODE HERE
    //NOTE: since the data source return random values with each request,
    //there's no point on persisting in the database

}

val viewModelModule = module {
    viewModel { ProductTransactionViewModel(get()) }
}

val utilsModule = module {
    //TODO: YOUR CODE HERE
}