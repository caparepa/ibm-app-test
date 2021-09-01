package com.example.international.business.men.di.modules

import android.content.Context
import com.example.international.business.men.data.InternationalBusinessMenDatabase
import com.example.international.business.men.data.db.dao.ProductDao
import com.example.international.business.men.network.api.ApiClient
import com.example.international.business.men.network.interceptor.ConnectivityInterceptor
import com.example.international.business.men.network.interceptor.ConnectivityInterceptorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient.invoke() }
    single<ConnectivityInterceptor> { ConnectivityInterceptorImpl(get()) }
}

val repositoryModule = module {

}

val localModule = module {

}

val dataModule = module {

}

val databaseModule = module {
    single { InternationalBusinessMenDatabase.invoke(androidContext()) }

    fun provideProductDao(database: InternationalBusinessMenDatabase): ProductDao {
        return database.getProductDao()
    }

    single { provideProductDao(get()) }
}

val viewModelModule = module {

}

val utilsModule = module {

}