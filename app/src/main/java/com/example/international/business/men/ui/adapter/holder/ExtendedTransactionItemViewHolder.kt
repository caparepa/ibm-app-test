package com.example.international.business.men.ui.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.international.business.men.R
import com.example.international.business.men.ui.adapter.base.DynamicAdapterViewHolder
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.ExtendedTransactionItemModel
import com.example.international.business.men.utils.setOneOffClickListener

class ExtendedTransactionItemViewHolder(private val view: View) :
    DynamicAdapterViewHolder<ExtendedTransactionItemModel>(view) {

    lateinit var item: ExtendedTransactionItemModel
    lateinit var onClick: (item: ExtendedTransactionItemModel, action: String) -> Unit

    private val itemLayout = itemView.findViewById<ConstraintLayout>(R.id.clItemLayout)
    private val skuValue = itemView.findViewById<TextView>(R.id.tvTxSkuValue)
    private val amountOriginal = itemView.findViewById<TextView>(R.id.tvAmountOriginal)
    private val amountConverted = itemView.findViewById<TextView>(R.id.tvAmountConverted)

    override fun bind(
        item: ExtendedTransactionItemModel,
        position: Int,
        onClick: (ItemModel, String) -> Unit
    ) {
        this.item = item
        this.onClick = onClick

        val clItemLayout = itemLayout
        val tvSkuValue = skuValue
        val tvAmountOriginal = amountOriginal
        val tvAmountConverted = amountConverted

        val amountOriginal = "${item.model.currency} ${item.model.amount}"
        val amountConverted = "(${item.model.convertedCurrency} ${item.model.convertedAmount})"

        tvSkuValue.text = item.model.sku
        tvAmountOriginal.text = amountOriginal
        tvAmountConverted.text = amountConverted

        clItemLayout.setOneOffClickListener {
            this.onClick.invoke(item, "no_action")
        }
    }
}