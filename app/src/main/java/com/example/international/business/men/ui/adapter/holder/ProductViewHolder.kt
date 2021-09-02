package com.example.international.business.men.ui.adapter.holder

import android.view.View
import com.example.international.business.men.ui.adapter.base.DynamicAdapterViewHolder
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.ProductItemModel

class ProductViewHolder(private val view: View) :
    DynamicAdapterViewHolder<ProductItemModel>(view) {
    override fun bind(item: ProductItemModel, position: Int, onClick: (ItemModel, String) -> Unit) {
        TODO("Not yet implemented")
    }
}