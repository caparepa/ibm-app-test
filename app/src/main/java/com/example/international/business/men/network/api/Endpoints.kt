package com.example.international.business.men.network.api

import com.example.international.business.men.data.model.ExchangeRateItem
import com.example.international.business.men.data.model.TransactionItem
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET

interface Endpoints {

    @GET("/rates.json")
    suspend fun getExchangeRates(): Response<List<ExchangeRateItem>?>

    @GET("/transactions.json")
    suspend fun getTransactionList(): Response<List<TransactionItem>?>

}