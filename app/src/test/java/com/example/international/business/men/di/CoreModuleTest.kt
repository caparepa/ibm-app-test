package com.example.international.business.men.di

import com.example.international.business.men.di.modules.repositoryModule
import com.example.international.business.men.di.modules.utilityModule

fun configureAppTestModules(baseUrl: String) = listOf(
    mockWebServerTestModule,
    networkTestModule(baseUrl),
    repositoryModule,
    utilityModule
    )