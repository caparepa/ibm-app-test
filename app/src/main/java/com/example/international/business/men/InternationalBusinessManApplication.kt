package com.example.international.business.men

import android.app.Application
import com.example.international.business.men.di.CoreModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class InternationalBusinessManApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        CoreModule.init()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@InternationalBusinessManApplication)
        }
    }

}