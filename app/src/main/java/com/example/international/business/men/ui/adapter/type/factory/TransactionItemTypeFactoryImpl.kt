package com.example.international.business.men.ui.adapter.type.factory

import android.view.View
import com.example.international.business.men.R
import com.example.international.business.men.data.model.ExtendedTransactionItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.ui.adapter.base.DynamicAdapterViewHolder
import com.example.international.business.men.ui.adapter.holder.ExtendedTransactionItemViewHolder
import com.example.international.business.men.ui.adapter.holder.ProductViewHolder
import com.example.international.business.men.ui.adapter.holder.TransactionItemViewHolder

class TransactionItemTypeFactoryImpl : TransactionItemTypeFactory {
    override fun typeExtendedTransactionItem(type: ExtendedTransactionItem): Int = R.layout.item_extended_transaction_list

    override fun typeTransactionItem(type: TransactionItem): Int = R.layout.item_transaction_list

    override fun typeProduct(type: TransactionItem): Int = R.layout.item_product_list

    override fun holder(type: Int, view: View): DynamicAdapterViewHolder<*> {
        return when(type) {
            R.layout.item_extended_transaction_list -> ExtendedTransactionItemViewHolder(view)
            R.layout.item_transaction_list -> TransactionItemViewHolder(view)
            R.layout.item_product_list -> ProductViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}