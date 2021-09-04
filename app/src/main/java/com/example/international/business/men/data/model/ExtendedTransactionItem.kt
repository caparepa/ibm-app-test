package com.example.international.business.men.data.model

import com.google.gson.annotations.SerializedName

data class ExtendedTransactionItem(
    val sku: String?,
    val amount: String?,
    val currency: String?,
    val convertedCurrency: String?,
    val convertedAmount: String?
)
