package com.example.international.business.men.data.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TransactionItem(
    @SerializedName("sku")
    val sku: String?,
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("currency")
    val currency: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sku)
        parcel.writeString(amount)
        parcel.writeString(currency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionItem> {
        override fun createFromParcel(parcel: Parcel): TransactionItem {
            return TransactionItem(parcel)
        }

        override fun newArray(size: Int): Array<TransactionItem?> {
            return arrayOfNulls(size)
        }
    }
}