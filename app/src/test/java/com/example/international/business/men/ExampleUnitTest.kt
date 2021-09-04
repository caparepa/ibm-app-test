package com.example.international.business.men

import com.example.international.business.men.data.model.ExchangeRateItem
import org.junit.Test

import org.junit.Assert.*
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.utils.CURRENCY_EUR
import com.example.international.business.men.utils.permutations
import com.example.international.business.men.utils.roundToHalfEven


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun assert_listNotEmpty() {

        val mockSku = "C2149"
        val mockToCurrency = CURRENCY_EUR

        //obtain mock rate list
        val rateList = initExchangeRateList()

        //obtain mock transaction list
        val allTransactionList = initTransactionList()

        //filter transactions by sku
        var filteredTransactionList = getTransactionsBySku(mockSku, allTransactionList)

        val missingRatesAux = getMissingCurrencyRatesAuxList(mockToCurrency, rateList)
        println("missingRates")
        missingRatesAux.forEach { item ->
            println(item)
        }
        val totalSum = getSkuTransactionsAmountSum(mockToCurrency, missingRatesAux, allTransactionList)
        val missingRatesPrime = getMissingCurrencyRatesFunctional("EUR", rateList)
        println("totalsum $totalSum")

    }

    private fun getMissingCurrencyRatesFunctional(
        currencyTo: String,
        list: List<ExchangeRateItem>
    ): List<ExchangeRateItem> {

        var result: List<ExchangeRateItem> = arrayListOf<ExchangeRateItem>()

        //Converto list to mutable list
        val modList = list.toMutableList()

        //obtain a set with all currency signs
        val currencySet = getCurrencySet(list)

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
        result = calculateRatesFunctional(currencyTo, modList)

        //return result
        return result
    }

    fun calculateRatesFunctional(
        currencyCond: String,
        modList: MutableList<ExchangeRateItem>
    ): List<ExchangeRateItem> {

        //since the list is mutable from the start, we can work with it
        modList.forEach { _ ->
            //we obtain the first null rate for the intended currency pair (e.g. AUD-EUR)
            val missingRate =
                modList.firstOrNull { ini -> ini.rate == null && ini.to!! == currencyCond }
            //
            if (missingRate != null) {
                //if pair is foud, look for a pivot (e.g. AUD-CAD (this is the pivot) -> CAD-EUR)
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

    private fun getCurrencySet(list: List<ExchangeRateItem>): Set<String> {
        val currencySet = mutableSetOf<String>()
        list.forEach { item ->
            currencySet.add(item.from!!)
            currencySet.add(item.to!!)
        }
        return currencySet
    }

    private fun getTransactionsBySku(
        sku: String,
        list: List<TransactionItem>?
    ): List<TransactionItem>? {
        return list?.filter { item -> item.sku == sku }
    }

    private fun getMissingCurrencyRatesAuxList(
        currencyTo: String,
        list: List<ExchangeRateItem>
    ): List<ExchangeRateItem> {
        //convert to mutable list
        var fromAny = list.toMutableList()

        //create another list with wanted end currency rate
        var toEndCurr: MutableList<ExchangeRateItem> =
            fromAny.filter { item -> item.to == currencyTo } as MutableList<ExchangeRateItem>

        //remove all wanted currency pais from original list
        fromAny.removeAll { item -> item.from == currencyTo }
        fromAny.removeAll { item -> item.to == currencyTo }

        //iterate over wanted currency list and validate
        var endCurrIndex = 0
        while (endCurrIndex < toEndCurr.size) {
            var anyIndex = 0
            while (anyIndex < fromAny.size) {
                //validate if a missing rate has the same originating currency sign e.g. CAD - EUR => CAD - AUD
                if (fromAny[anyIndex].from == toEndCurr[endCurrIndex].from) {
                    //calculate new rate and add new objecto to wanted rate list
                    val tempRate =
                        toEndCurr[endCurrIndex].rate!!.toDouble() / fromAny[anyIndex].rate!!.toDouble()
                    val newRate = tempRate.roundToHalfEven()
                    val newToEndCurr = ExchangeRateItem(
                        from = fromAny[anyIndex].to,
                        to = toEndCurr[endCurrIndex].to,
                        rate = newRate.toString()
                    )
                    toEndCurr.add(newToEndCurr)
                    fromAny.remove(fromAny[anyIndex + 1])
                    fromAny.remove(fromAny[anyIndex])
                } else {
                    anyIndex++
                }
            }
            endCurrIndex++
        }

        return toEndCurr
    }

    private fun getSkuTransactionsAmountSum(
        currencyTo: String,
        rates: List<ExchangeRateItem>,
        list: List<TransactionItem>?
    ): Double {
        var result: Double = 0.0

        val currencySet = getCurrencySet(rates)

        val newList = list?.filter { item ->
            currencySet.contains(item.currency!!)
        }

        newList?.forEach { item ->
            result += if (item.currency == "EUR") {
                item.amount!!.toDouble()
            } else {
                val rateItem = rates.first { elem -> elem.from == item.currency }
                val rate = rateItem.rate!!.toDouble()
                (item.amount!!.toDouble() * rate).roundToHalfEven()
            }
        }

        return result.roundToHalfEven()
    }

    private fun initExchangeRateList(): List<ExchangeRateItem> {
        var list = arrayListOf<ExchangeRateItem>()
        list.add(ExchangeRateItem(from = "CAD", to = "AUD", rate = "0.65"))
        list.add(ExchangeRateItem(from = "AUD", to = "CAD", rate = "1.54"))
        list.add(ExchangeRateItem(from = "CAD", to = "EUR", rate = "0.63"))
        list.add(ExchangeRateItem(from = "EUR", to = "CAD", rate = "1.59"))
        list.add(ExchangeRateItem(from = "AUD", to = "USD", rate = "0.73"))
        list.add(ExchangeRateItem(from = "USD", to = "AUD", rate = "1.37"))
        return list
    }

    private fun initTransactionList(): List<TransactionItem> {

        var list = arrayListOf<TransactionItem>()

        list.add(TransactionItem(sku = "C2149", amount = "18.7", currency = "AUD"))
        list.add(TransactionItem(sku = "L7071", amount = "21.5", currency = "EUR"))
        list.add(TransactionItem(sku = "N6330", amount = "33.1", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "32.6", currency = "USD"))
        list.add(TransactionItem(sku = "F1055", amount = "25.6", currency = "AUD"))
        list.add(TransactionItem(sku = "N3391", amount = "25.9", currency = "USD"))
        list.add(TransactionItem(sku = "W1437", amount = "28.6", currency = "CAD"))
        list.add(TransactionItem(sku = "N6330", amount = "16.1", currency = "EUR"))
        list.add(TransactionItem(sku = "W1437", amount = "25.3", currency = "CAD"))
        list.add(TransactionItem(sku = "F1055", amount = "16.0", currency = "AUD"))
        list.add(TransactionItem(sku = "W1437", amount = "27.5", currency = "EUR"))
        list.add(TransactionItem(sku = "N5844", amount = "15.6", currency = "CAD"))
        list.add(TransactionItem(sku = "F1055", amount = "29.5", currency = "AUD"))
        list.add(TransactionItem(sku = "N6330", amount = "27.0", currency = "EUR"))
        list.add(TransactionItem(sku = "N6330", amount = "26.4", currency = "EUR"))
        list.add(TransactionItem(sku = "M6639", amount = "20.9", currency = "USD"))
        list.add(TransactionItem(sku = "I6375", amount = "27.7", currency = "USD"))
        list.add(TransactionItem(sku = "K3549", amount = "28.0", currency = "CAD"))
        list.add(TransactionItem(sku = "E0510", amount = "30.8", currency = "AUD"))
        list.add(TransactionItem(sku = "I6375", amount = "15.6", currency = "AUD"))
        list.add(TransactionItem(sku = "C2149", amount = "32.9", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "15.3", currency = "CAD"))
        list.add(TransactionItem(sku = "B6134", amount = "20.3", currency = "EUR"))
        list.add(TransactionItem(sku = "C4028", amount = "26.0", currency = "AUD"))
        list.add(TransactionItem(sku = "U4760", amount = "22.6", currency = "EUR"))
        list.add(TransactionItem(sku = "W1437", amount = "25.6", currency = "CAD"))
        list.add(TransactionItem(sku = "I6375", amount = "31.1", currency = "EUR"))
        list.add(TransactionItem(sku = "C2149", amount = "27.2", currency = "AUD"))
        list.add(TransactionItem(sku = "U4760", amount = "20.4", currency = "AUD"))
        list.add(TransactionItem(sku = "C2149", amount = "33.3", currency = "EUR"))
        list.add(TransactionItem(sku = "K3549", amount = "21.7", currency = "USD"))
        list.add(TransactionItem(sku = "U4760", amount = "30.8", currency = "CAD"))
        list.add(TransactionItem(sku = "W1437", amount = "22.7", currency = "USD"))
        list.add(TransactionItem(sku = "N6330", amount = "28.3", currency = "USD"))
        list.add(TransactionItem(sku = "F1055", amount = "15.8", currency = "CAD"))
        list.add(TransactionItem(sku = "N5844", amount = "21.9", currency = "USD"))
        list.add(TransactionItem(sku = "F1055", amount = "23.3", currency = "USD"))
        list.add(TransactionItem(sku = "N5844", amount = "31.3", currency = "AUD"))
        list.add(TransactionItem(sku = "C4028", amount = "19.8", currency = "AUD"))
        list.add(TransactionItem(sku = "I6375", amount = "30.5", currency = "EUR"))
        list.add(TransactionItem(sku = "U4760", amount = "27.3", currency = "AUD"))
        list.add(TransactionItem(sku = "F1055", amount = "32.2", currency = "USD"))
        list.add(TransactionItem(sku = "C2149", amount = "26.0", currency = "CAD"))
        list.add(TransactionItem(sku = "C4028", amount = "32.6", currency = "EUR"))
        list.add(TransactionItem(sku = "E0510", amount = "22.8", currency = "USD"))
        list.add(TransactionItem(sku = "N9193", amount = "16.4", currency = "USD"))
        list.add(TransactionItem(sku = "N5844", amount = "19.2", currency = "AUD"))
        list.add(TransactionItem(sku = "N6330", amount = "22.4", currency = "EUR"))
        list.add(TransactionItem(sku = "E0510", amount = "28.2", currency = "USD"))
        list.add(TransactionItem(sku = "F1055", amount = "34.9", currency = "CAD"))
        list.add(TransactionItem(sku = "L7071", amount = "34.3", currency = "EUR"))
        list.add(TransactionItem(sku = "L7071", amount = "16.3", currency = "AUD"))
        list.add(TransactionItem(sku = "E0510", amount = "29.3", currency = "CAD"))
        list.add(TransactionItem(sku = "I6375", amount = "26.7", currency = "EUR"))
        list.add(TransactionItem(sku = "M6639", amount = "16.2", currency = "USD"))
        list.add(TransactionItem(sku = "C2149", amount = "32.3", currency = "USD"))
        list.add(TransactionItem(sku = "M6639", amount = "17.5", currency = "CAD"))
        list.add(TransactionItem(sku = "F1055", amount = "21.0", currency = "EUR"))
        list.add(TransactionItem(sku = "C2149", amount = "17.7", currency = "CAD"))
        list.add(TransactionItem(sku = "N9193", amount = "22.2", currency = "USD"))
        list.add(TransactionItem(sku = "B6134", amount = "27.7", currency = "AUD"))
        list.add(TransactionItem(sku = "L7071", amount = "34.9", currency = "EUR"))
        list.add(TransactionItem(sku = "N6330", amount = "33.3", currency = "CAD"))
        list.add(TransactionItem(sku = "F1055", amount = "19.3", currency = "AUD"))
        list.add(TransactionItem(sku = "E0510", amount = "30.9", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "28.7", currency = "EUR"))
        list.add(TransactionItem(sku = "I6375", amount = "31.1", currency = "CAD"))
        list.add(TransactionItem(sku = "E0510", amount = "21.4", currency = "CAD"))
        list.add(TransactionItem(sku = "N5844", amount = "34.1", currency = "CAD"))
        list.add(TransactionItem(sku = "C4028", amount = "34.1", currency = "EUR"))
        list.add(TransactionItem(sku = "F1055", amount = "33.3", currency = "USD"))
        list.add(TransactionItem(sku = "N9193", amount = "17.1", currency = "AUD"))
        list.add(TransactionItem(sku = "M6639", amount = "28.4", currency = "AUD"))
        list.add(TransactionItem(sku = "C2149", amount = "34.3", currency = "AUD"))
        list.add(TransactionItem(sku = "N5844", amount = "28.9", currency = "EUR"))
        list.add(TransactionItem(sku = "W1437", amount = "32.1", currency = "AUD"))
        list.add(TransactionItem(sku = "L7071", amount = "17.2", currency = "CAD"))
        list.add(TransactionItem(sku = "B6134", amount = "15.1", currency = "CAD"))
        list.add(TransactionItem(sku = "C2149", amount = "21.9", currency = "EUR"))
        list.add(TransactionItem(sku = "N9193", amount = "27.2", currency = "AUD"))
        list.add(TransactionItem(sku = "I6375", amount = "15.2", currency = "CAD"))
        list.add(TransactionItem(sku = "B6134", amount = "26.9", currency = "EUR"))
        list.add(TransactionItem(sku = "K3549", amount = "20.2", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "16.2", currency = "CAD"))
        list.add(TransactionItem(sku = "L7071", amount = "28.5", currency = "AUD"))
        list.add(TransactionItem(sku = "L7071", amount = "25.8", currency = "EUR"))
        list.add(TransactionItem(sku = "C2149", amount = "27.7", currency = "EUR"))
        list.add(TransactionItem(sku = "K3549", amount = "16.9", currency = "EUR"))
        list.add(TransactionItem(sku = "U4760", amount = "19.5", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "28.8", currency = "AUD"))
        list.add(TransactionItem(sku = "N9193", amount = "30.9", currency = "CAD"))
        list.add(TransactionItem(sku = "L7071", amount = "22.3", currency = "EUR"))
        list.add(TransactionItem(sku = "E0510", amount = "22.9", currency = "AUD"))
        list.add(TransactionItem(sku = "N3391", amount = "16.2", currency = "AUD"))
        list.add(TransactionItem(sku = "N9193", amount = "32.3", currency = "USD"))
        list.add(TransactionItem(sku = "M6639", amount = "31.4", currency = "EUR"))
        list.add(TransactionItem(sku = "L7071", amount = "19.0", currency = "CAD"))
        list.add(TransactionItem(sku = "N5844", amount = "22.6", currency = "AUD"))
        list.add(TransactionItem(sku = "C4028", amount = "34.5", currency = "AUD"))
        list.add(TransactionItem(sku = "N3391", amount = "15.4", currency = "AUD"))
        list.add(TransactionItem(sku = "M6639", amount = "24.0", currency = "CAD"))
        list.add(TransactionItem(sku = "E0510", amount = "33.4", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "32.0", currency = "CAD"))
        list.add(TransactionItem(sku = "U4760", amount = "20.7", currency = "AUD"))
        list.add(TransactionItem(sku = "C2149", amount = "29.7", currency = "CAD"))
        list.add(TransactionItem(sku = "N9193", amount = "20.3", currency = "EUR"))
        list.add(TransactionItem(sku = "K3549", amount = "32.8", currency = "CAD"))
        list.add(TransactionItem(sku = "V5497", amount = "22.7", currency = "CAD"))
        list.add(TransactionItem(sku = "U4760", amount = "23.5", currency = "USD"))
        list.add(TransactionItem(sku = "B6134", amount = "28.9", currency = "CAD"))
        list.add(TransactionItem(sku = "F1055", amount = "20.8", currency = "EUR"))
        list.add(TransactionItem(sku = "K3549", amount = "34.2", currency = "CAD"))
        list.add(TransactionItem(sku = "C4028", amount = "26.8", currency = "USD"))
        list.add(TransactionItem(sku = "N3391", amount = "22.3", currency = "AUD"))
        list.add(TransactionItem(sku = "N5844", amount = "34.2", currency = "AUD"))
        list.add(TransactionItem(sku = "N5844", amount = "16.9", currency = "CAD"))
        list.add(TransactionItem(sku = "N5844", amount = "31.6", currency = "USD"))
        list.add(TransactionItem(sku = "B6134", amount = "25.2", currency = "CAD"))
        list.add(TransactionItem(sku = "N3391", amount = "31.6", currency = "USD"))
        list.add(TransactionItem(sku = "I6375", amount = "19.7", currency = "EUR"))
        list.add(TransactionItem(sku = "N3391", amount = "20.4", currency = "CAD"))
        list.add(TransactionItem(sku = "U4760", amount = "17.7", currency = "EUR"))
        list.add(TransactionItem(sku = "I6375", amount = "24.0", currency = "USD"))
        list.add(TransactionItem(sku = "C2149", amount = "22.5", currency = "AUD"))
        list.add(TransactionItem(sku = "B6134", amount = "15.5", currency = "USD"))
        list.add(TransactionItem(sku = "K3549", amount = "32.1", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "17.0", currency = "EUR"))
        list.add(TransactionItem(sku = "N6330", amount = "17.5", currency = "CAD"))
        list.add(TransactionItem(sku = "N6330", amount = "18.5", currency = "CAD"))
        list.add(TransactionItem(sku = "N6330", amount = "17.3", currency = "CAD"))
        list.add(TransactionItem(sku = "N9193", amount = "19.7", currency = "USD"))
        list.add(TransactionItem(sku = "B6134", amount = "23.5", currency = "USD"))
        list.add(TransactionItem(sku = "C4028", amount = "32.9", currency = "USD"))
        list.add(TransactionItem(sku = "C4028", amount = "19.6", currency = "USD"))
        list.add(TransactionItem(sku = "N3391", amount = "20.4", currency = "USD"))
        list.add(TransactionItem(sku = "K3549", amount = "25.6", currency = "AUD"))
        list.add(TransactionItem(sku = "F1055", amount = "26.0", currency = "USD"))
        list.add(TransactionItem(sku = "N3391", amount = "17.8", currency = "CAD"))
        list.add(TransactionItem(sku = "C2149", amount = "20.2", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "17.7", currency = "AUD"))
        list.add(TransactionItem(sku = "B6134", amount = "22.8", currency = "AUD"))
        list.add(TransactionItem(sku = "B6134", amount = "25.9", currency = "EUR"))
        list.add(TransactionItem(sku = "N6330", amount = "25.3", currency = "AUD"))
        list.add(TransactionItem(sku = "C4028", amount = "16.9", currency = "CAD"))
        list.add(TransactionItem(sku = "F1055", amount = "30.3", currency = "USD"))
        list.add(TransactionItem(sku = "I6375", amount = "19.1", currency = "EUR"))
        list.add(TransactionItem(sku = "W1437", amount = "28.8", currency = "USD"))
        list.add(TransactionItem(sku = "W1437", amount = "27.2", currency = "EUR"))
        list.add(TransactionItem(sku = "I6375", amount = "15.3", currency = "USD"))
        list.add(TransactionItem(sku = "U4760", amount = "23.4", currency = "CAD"))
        list.add(TransactionItem(sku = "L7071", amount = "24.1", currency = "EUR"))
        list.add(TransactionItem(sku = "L7071", amount = "19.6", currency = "USD"))
        list.add(TransactionItem(sku = "K3549", amount = "28.5", currency = "CAD"))
        list.add(TransactionItem(sku = "W1437", amount = "33.8", currency = "CAD"))
        list.add(TransactionItem(sku = "M6639", amount = "16.5", currency = "AUD"))
        list.add(TransactionItem(sku = "B6134", amount = "31.1", currency = "CAD"))
        list.add(TransactionItem(sku = "V5497", amount = "30.4", currency = "AUD"))
        list.add(TransactionItem(sku = "N3391", amount = "27.7", currency = "USD"))
        list.add(TransactionItem(sku = "N3391", amount = "31.1", currency = "EUR"))
        list.add(TransactionItem(sku = "N5844", amount = "15.3", currency = "AUD"))
        list.add(TransactionItem(sku = "C4028", amount = "16.8", currency = "EUR"))
        list.add(TransactionItem(sku = "N6330", amount = "28.0", currency = "AUD"))
        list.add(TransactionItem(sku = "K3549", amount = "29.5", currency = "USD"))
        list.add(TransactionItem(sku = "C2149", amount = "27.1", currency = "USD"))
        list.add(TransactionItem(sku = "N9193", amount = "32.0", currency = "USD"))
        list.add(TransactionItem(sku = "N3391", amount = "33.6", currency = "CAD"))
        list.add(TransactionItem(sku = "W1437", amount = "31.4", currency = "EUR"))
        list.add(TransactionItem(sku = "B6134", amount = "25.1", currency = "USD"))
        list.add(TransactionItem(sku = "C4028", amount = "22.6", currency = "USD"))
        list.add(TransactionItem(sku = "N3391", amount = "20.5", currency = "AUD"))
        list.add(TransactionItem(sku = "I6375", amount = "22.8", currency = "CAD"))
        list.add(TransactionItem(sku = "N6330", amount = "28.3", currency = "USD"))
        list.add(TransactionItem(sku = "E0510", amount = "15.3", currency = "USD"))

        return list
    }

}