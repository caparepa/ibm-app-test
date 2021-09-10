package com.example.international.business.men.utils

import com.example.international.business.men.data.model.ExchangeRateItem

class ExchangeRateItemUtils {
    /**
     * Obtain missing exchange pairs
     */
    fun obtainMissingExchangePairs(
        list: List<ExchangeRateItem>,
        currencyTo: String
    ): MutableList<ExchangeRateItem> {
        //Convert to list to mutable list
        var modList = list.toMutableList()

        //obtain a set with all currency signs
        val currencySet = list.getCurrencySet()

        //obtain all permutations from the currency set
        val permutations = currencySet.permutations()

        //add the missing rate permutations with null rate to the mutable list
        modList = buildRatesList(permutations, currencyTo)

        return modList
    }

    fun buildRatesList(
        permutations: Sequence<List<String>>,
        currencyTo: String
    ): MutableList<ExchangeRateItem> {
        val modList = mutableListOf<ExchangeRateItem>()
        permutations.forEach { item ->
            val r = modList.firstOrNull() { elem -> elem.from == item[0] && elem.to == item[1] }
            if (r == null && item[1] == currencyTo) {
                modList.add(ExchangeRateItem(from = item[0], to = item[1], rate = null))
            }
        }

        return modList
    }

    /**
     * Calculate missing rates (functional)
     */
    fun calculateMissingExchangeRates(
        currencyCond: String,
        modList: MutableList<ExchangeRateItem>
    ): List<ExchangeRateItem> {

        //Create list with missing rates to iterate in (more efficient, less iterations)
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