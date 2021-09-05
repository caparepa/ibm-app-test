package com.example.international.business.men.ui.adapter.type.factory

import com.example.international.business.men.data.model.ExtendedTransactionItem
import com.example.international.business.men.data.model.TransactionItem
import com.example.international.business.men.ui.adapter.base.BaseTypeFactory

interface TransactionItemTypeFactory: BaseTypeFactory {
    fun typeExtendedTransactionItem(type: ExtendedTransactionItem): Int
    fun typeProduct(type: TransactionItem): Int
}