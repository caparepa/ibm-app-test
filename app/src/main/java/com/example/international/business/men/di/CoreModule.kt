package com.example.international.business.men.di

import com.example.international.business.men.di.modules.*
import org.koin.core.context.GlobalContext.loadKoinModules

object CoreModule {
    private val modules = listOf(
        networkModule,
        repositoryModule,
        localModule,
        dataModule,
        databaseModule,
        viewModelModule,
        utilsModule
    )

    fun init() = loadKoinModules(modules)
}