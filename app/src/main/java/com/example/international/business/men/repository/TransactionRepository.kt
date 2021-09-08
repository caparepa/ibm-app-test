package com.example.international.business.men.repository

import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.ExtendedTransactionItem
import com.example.international.business.men.data.model.TransactionItem

interface TransactionRepository {

    suspend fun getTransactionList(): List<TransactionItem>?

}