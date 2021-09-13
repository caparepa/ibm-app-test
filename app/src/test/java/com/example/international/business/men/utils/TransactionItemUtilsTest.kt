package com.example.international.business.men.utils

import com.example.international.business.men.base.BaseUTTest
import org.junit.Assert.*

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class TransactionItemUtilsTest: BaseUTTest(), KoinTest {

    private val transactionItemUtils: TransactionItemUtils by inject()
    private val currency = CURRENCY_EUR

    @Test
    fun getUniqueSkuList() {
    }

    @Test
    fun getTransactionsBySku() {
    }

    @Test
    fun getSkuTransactionsAmountSum() {
    }

    @Test
    fun getExtendedTransactionList() {
    }
}