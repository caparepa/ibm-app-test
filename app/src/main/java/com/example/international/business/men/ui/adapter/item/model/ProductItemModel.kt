package com.example.international.business.men.ui.adapter.item.model

import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.ui.adapter.base.BaseTypeFactory
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.type.factory.TransactionItemTypeFactory

class ProductItemModel(val model: TransactionItem): ItemModel() {
    override fun type(typeFactory: BaseTypeFactory): Int {
        return (typeFactory as TransactionItemTypeFactory).typeProduct(model)
    }
}