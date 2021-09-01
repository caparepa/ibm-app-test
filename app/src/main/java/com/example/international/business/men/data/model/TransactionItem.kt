package com.example.international.business.men.data.model


import com.google.gson.annotations.SerializedName

data class TransactionItem(
    @SerializedName("sku")
    val sku: String?,
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("currency")
    val currency: String?
)