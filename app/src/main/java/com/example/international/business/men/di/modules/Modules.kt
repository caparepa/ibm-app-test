package com.example.international.business.men.di.modules

import com.example.international.business.men.data.InternationalBusinessMenDatabase
import com.example.international.business.men.data.db.dao.ProductDao
import com.example.international.business.men.network.api.ApiClient
import com.example.international.business.men.network.interceptor.ConnectivityInterceptor
import com.example.international.business.men.network.interceptor.ConnectivityInterceptorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient.invoke() }
    single<ConnectivityInterceptor> { ConnectivityInterceptorImpl(get()) }
}

val repositoryModule = module {
    //TODO: YOUR CODE HERE
}

val localModule = module {
    //TODO: YOUR CODE HERE
}

val dataModule = module {
    //TODO: YOUR CODE HERE
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
    //TODO: YOUR CODE HERE
}

val utilsModule = module {
    //TODO: YOUR CODE HERE
}