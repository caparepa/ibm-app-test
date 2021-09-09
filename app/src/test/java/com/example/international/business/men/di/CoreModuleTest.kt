package com.example.international.business.men.di

import com.example.international.business.men.di.modules.repositoryModule
import com.example.international.business.men.di.modules.utilityModule
import com.example.international.business.men.di.modules.viewModelModule

fun configureAppTestModules(baseUrl: String) = listOf(
    mockWebServerTestModule,
    networkTestModule(baseUrl),
    repositoryModule,
    viewModelModule,
    utilityModule
    )