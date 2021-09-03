package com.example.international.business.men.repository

import android.util.Log
import com.example.international.business.men.data.db.dao.ExchangeRateDao
import com.example.international.business.men.data.db.entity.ExchangeRateEntity
import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.network.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExchangeRateRepositoryImpl() : ExchangeRateRepository, KoinComponent {

    private val api = ApiClient.invoke()

    override suspend fun getExchangeRates(): List<ExchangeRateItem>? = withContext(Dispatchers.IO){
        val response = api.getExchangeRates()
        val list = response.body()
        list
    }

    override fun getMissingCurrencyRates(to: String, list: List<ExchangeRateItem>): List<ExchangeRateItem> {
        //0: pasar a mutable list
        var fromAny = list.toMutableList()

        //1: sacar to=EUR a otra lista
        var toEur: MutableList<ExchangeRateItem> = fromAny.filter { item -> item.to == "EUR" } as MutableList<ExchangeRateItem>

        //2: eliminar from=EUR de fromAny
        fromAny.removeAll { item -> item.from == to }
        fromAny.removeAll { item -> item.to == to }

        //3: iterar sobre toEur y validar sobre fromAny
        var eurIndex = 0
        while (eurIndex < toEur.size) {
            var anyIndex = 0
            while (anyIndex < fromAny.size) {
                //e.g. CAD - EUR => CAD - AUD
                if(fromAny[anyIndex].from == toEur[eurIndex].from) {
                    val newRate = toEur[eurIndex].rate!!.toDouble() / fromAny[anyIndex].rate!!.toDouble()
                    val newToEur = ExchangeRateItem(
                        from = fromAny[anyIndex].to,
                        to = toEur[eurIndex].to,
                        rate = newRate.toString()
                    )
                    toEur.add(newToEur)
                    fromAny.remove(fromAny[anyIndex+1])
                    fromAny.remove(fromAny[anyIndex])
                    //result here is adding AUD - EUR
                } else {
                    anyIndex++
                }
            }
            eurIndex++
        }

        return toEur
    }
}