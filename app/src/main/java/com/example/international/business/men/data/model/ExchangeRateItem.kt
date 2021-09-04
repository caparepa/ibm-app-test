package com.example.international.business.men.data.model


import com.google.gson.annotations.SerializedName

data class ExchangeRateItem(
    @SerializedName("from")
    val from: String?,
    @SerializedName("to")
    val to: String?,
    @SerializedName("rate")
    val rate: String?
) {
    override fun toString(): String {
        return "[from: $from to: $to rate: $rate]\n"
    }
}