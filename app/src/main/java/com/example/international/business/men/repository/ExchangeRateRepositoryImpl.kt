package com.example.international.business.men.repository

import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.network.api.ApiClient
import com.example.international.business.men.utils.getCurrencySet
import com.example.international.business.men.utils.permutations
import com.example.international.business.men.utils.roundToHalfEven
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class ExchangeRateRepositoryImpl() : ExchangeRateRepository, KoinComponent {

    private val api = ApiClient.invoke()

    override suspend fun getExchangeRates(): List<ExchangeRateItem>? = withContext(Dispatchers.IO){
        val response = api.getExchangeRates()
        val list = response.body()
        list
    }

    /**
     * Get the missing rates (functional)
     */
    override fun getMissingCurrencyRates(currencyTo: String, list: List<ExchangeRateItem>): List<ExchangeRateItem> {
        var result: List<ExchangeRateItem> = arrayListOf<ExchangeRateItem>()

        //Converto list to mutable list
        val modList = list.toMutableList()

        //obtain a set with all currency signs
        val currencySet = list.getCurrencySet()

        //obtain all permutations from the currency set
        val permutations = currencySet.permutations()

        //add the missing rate permutations with null rate to the mutable list
        permutations.forEach { item ->
            val r = modList.firstOrNull() { elem -> elem.from == item[0] && elem.to == item[1] }
            if (r == null && item[1] == currencyTo) {
                modList.add(ExchangeRateItem(from = item[0], to = item[1], rate = null))
            }
        }

        //calculate the missing (null) rates
        result = calculateRates(currencyTo, modList)

        //return result
        return result
    }

    /**
     * Calculate missing rates (functional)
     */
    private fun calculateRates(
        currencyCond: String,
        modList: MutableList<ExchangeRateItem>
    ): List<ExchangeRateItem> {

        //Create list with missing rates to iterate in (more efficient)
        val copyList = modList.filter { item -> item.rate == null }

        //since the original list is mutable from the start, we can work with it
        copyList.forEach { _ ->
            //we obtain the first null rate for the intended currency pair (e.g. AUD-EUR)
            val missingRate =
                modList.firstOrNull { ini -> ini.rate == null && ini.to!! == currencyCond }
            //
            if (missingRate != null) {
                //if pair is found, look for a pivot (e.g. AUD-CAD (this is the pivot) -> CAD-EUR)
                val pivotRate =
                    modList.firstOrNull { sec -> sec.rate != null && sec.from!! == missingRate.from!! }
                if (pivotRate != null) {
                    //if pivot is found, loof for the end rate (e.g. AUD-CAD -> CAD-EUR (this is the end rate)
                    val endRate =
                        modList.firstOrNull { ter -> ter.rate != null && ter.to!! == missingRate.to!! }
                    if (endRate != null) {
                        //if end rate is found, calculate missing rate
                        val result = endRate.rate!!.toDouble() * pivotRate.rate!!.toDouble()
                        //get current item index
                        val index = modList.indexOf(missingRate)
                        //update current object rate
                        modList[index].rate = result.roundToHalfEven().toString()
                    }
                }
            }
        }
        return modList
    }

}