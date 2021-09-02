package com.example.international.business.men.ui.adapter.type.factory

import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.ui.adapter.base.BaseTypeFactory

interface TransactionItemTypeFactory: BaseTypeFactory {
    fun typeTransactionItem(type: TransactionItem): Int
    fun typeProduct(type: TransactionItem): Int
}