package com.example.international.business.men.repository

import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.ExtendedTransactionItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.network.api.ApiClient
import com.example.international.business.men.utils.roundToHalfEven
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class TransactionRepositoryImpl() : TransactionRepository, KoinComponent {

    private val api = ApiClient.invoke()

    override suspend fun getTransactionList(): List<TransactionItem>? = withContext(Dispatchers.IO) {
        val response = api.getTransactionList()
        val list = response.body()
        list
    }

    override fun getUniqueSkuList(list: List<TransactionItem>?): List<TransactionItem>? {
        return list?.distinctBy { item -> item.sku }
    }

    override fun getTransactionsBySku(sku: String, list: List<TransactionItem>?): List<TransactionItem>? {
        return list?.filter { item -> item.sku == sku}
    }

    override fun getSkuTransactionsAmountSum(
        rates: List<ExchangeRateItem>,
        list: List<TransactionItem>?,
        currency: String
    ): Double {
        var result: Double = 0.0

        list?.forEach { item ->
            result += if(item.currency == currency) {
                item.amount!!.toDouble()
            } else {
                val rateItem = rates.first { it.from == item.currency }
                val rate = rateItem.rate!!.toDouble()
                (item.amount!!.toDouble() * rate).roundToHalfEven()
            }
        }

        return result.roundToHalfEven()
    }

    override fun getExtendedTransactionList(
        currency: String,
        rates: List<ExchangeRateItem>,
        list: List<TransactionItem>
    ): List<ExtendedTransactionItem> {
        var exList = arrayListOf<ExtendedTransactionItem>()

        list.forEach { item ->
            var conversion = 0.0
            if(item.currency == currency) {
                conversion = item.amount!!.toDouble()
            } else {
                val rateItem = rates.firstOrNull { it.from == item.currency }
                if(rateItem != null) {
                    val rate = rateItem.rate!!.toDouble()
                    conversion = (item.amount!!.toDouble() * rate).roundToHalfEven()
                }
            }
            val obj = ExtendedTransactionItem(
                sku = item.sku,
                amount = item.amount,
                currency = item.currency,
                convertedAmount = conversion.toString(),
                convertedCurrency = currency
            )
            exList.add(obj)
        }
        return exList
    }


}