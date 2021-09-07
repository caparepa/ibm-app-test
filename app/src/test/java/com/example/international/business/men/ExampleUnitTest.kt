package com.example.international.business.men

import com.example.international.business.men.base.BaseUTTest
import com.example.international.business.men.data.model.ExchangeRateItem
import org.junit.Test

import org.junit.Assert.*
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.utils.*
import com.google.gson.reflect.TypeToken


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest: BaseUTTest() {

    /**
     * mock data init
     */
    private fun initExchangeRateList(): List<ExchangeRateItem> {
        var list: List<ExchangeRateItem> = arrayListOf<ExchangeRateItem>()
        val dataJson = getJson("ibm_mock_rates.json")
        list = parseArray(dataJson, object: TypeToken<List<ExchangeRateItem>>() {}.type)
        return list
    }

    private fun initTransactionList(): List<TransactionItem> {
        var list: List<TransactionItem> = arrayListOf<TransactionItem>()
        val dataJson = getJson("ibm_mock_transactions.json")
        val type = object: TypeToken<List<TransactionItem>>() {}.type
        list = parseArray(dataJson, type)
        return list
    }

    private fun initRandomTransactionList(qty: Int): List<TransactionItem> {
        val list = arrayListOf<TransactionItem>()
        val currencyList = initExchangeRateList().getCurrencySet().toList()
        for (i in 0 .. qty) {
            list.add(
                TransactionItem(
                    sku = generateRandomSku(),
                    amount = generateRandomAmount(),
                    currency = getRandomCurrency(currencyList)
                )
            )
        }
        return list
    }

    @Test
    fun randomSku_isNotEmpty() {
        val randomSku: String = generateRandomSku()
        assertNotEquals("", randomSku)
    }

    @Test
    fun randomAmount_isNotEmpty() {
        val randomAmount: String = generateRandomAmount()
        assertNotEquals("", randomAmount)
    }

    @Test
    fun randomCurrency_isNotEmpty() {
        val rateList = initExchangeRateList()
        val randomCurrency = getRandomCurrency(rateList.getCurrencySet().toList())
        assertNotEquals("", randomCurrency)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun evaluate_missingRatesAux() {

        val mockSku = "R3990"
        val mockToCurrency = CURRENCY_EUR

        //obtain mock rate list
        val rateList = initExchangeRateList()

        //obtain mock transaction list
        val allTransactionList = initTransactionList()

        //filter transactions by sku
        var filteredTransactionList: List<TransactionItem>? =
            measureTimeMillis({ time -> println("getTransactionsBySku (Aux) took $time") }) {
                getTransactionsBySku(mockSku, allTransactionList)
            }

        val missingRatesAux: List<ExchangeRateItem> =
            measureTimeMillis({ time -> println("getMissingCurrencyRatesAuxList(Aux) took $time") }) {
                getMissingCurrencyRatesAuxList(mockToCurrency, rateList)
            }

        val totalSum: Double =
            measureTimeMillis({ time -> println("getSkuTransactionsAmountSum (Aux) took $time") }) {
                getSkuTransactionsAmountSum(mockToCurrency, missingRatesAux, allTransactionList)
            }

    }

    @Test
    fun evaluate_missingRatesFun() {

        val mockSku = "R3990"
        val mockToCurrency = CURRENCY_EUR

        //obtain mock rate list
        val rateList = initExchangeRateList()

        //obtain mock transaction list
        val allTransactionList = initTransactionList()

        //filter transactions by sku
        var filteredTransactionList: List<TransactionItem>? =
            measureTimeMillis({ time -> println("getTransactionsBySku (Fun) took $time") }) {
                getTransactionsBySku(mockSku, allTransactionList)
            }

        val missingRatesAux: List<ExchangeRateItem> =
            measureTimeMillis({ time -> println("getMissingCurrencyRatesFunctional (Fun) took $time") }) {
                getMissingCurrencyRates(mockToCurrency, rateList)
            }

        val totalSum: Double =
            measureTimeMillis({ time -> println("getSkuTransactionsAmountSum (Fun) took $time") }) {
                getSkuTransactionsAmountSum(mockToCurrency, missingRatesAux, allTransactionList)
            }

    }

    /**
     * Get the missing rates (functional)
     */
    private fun getMissingCurrencyRates(
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

    /**
     * Get currency set
     */
    private fun getCurrencySet(list: List<ExchangeRateItem>): Set<String> {
        val currencySet = mutableSetOf<String>()
        list.forEach { item ->
            currencySet.add(item.from!!)
            currencySet.add(item.to!!)
        }
        return currencySet
    }

    /**
     * Get filtered transaction list by sku
     */
    private fun getTransactionsBySku(
        sku: String,
        list: List<TransactionItem>?
    ): List<TransactionItem>? {
        return list?.filter { item -> item.sku == sku }
    }

    /**
     * Get currency rate with auxiliar arrays
     */
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

    /**
     * Get transaction sum
     */
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

}