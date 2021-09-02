package com.example.international.business.men.ui.adapter.holder

import android.view.View
import com.example.international.business.men.ui.adapter.base.DynamicAdapterViewHolder
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.TransactionItemModel

class TransactionItemViewHolder(private val view: View) :
    DynamicAdapterViewHolder<TransactionItemModel>(view) {

    lateinit var item: TransactionItemModel
    lateinit var onClick: (item: TransactionItemModel, action: String) -> Unit



    override fun bind(
        item: TransactionItemModel,
        position: Int,
        onClick: (ItemModel, String) -> Unit
    ) {
        this.item = item
        this.onClick = onClick
    }
}