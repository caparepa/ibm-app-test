package com.example.international.business.men.ui.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.international.business.men.R
import com.example.international.business.men.ui.adapter.base.DynamicAdapterViewHolder
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.TransactionItemModel
import com.example.international.business.men.utils.setOneOffClickListener

class TransactionItemViewHolder(private val view: View) :
    DynamicAdapterViewHolder<TransactionItemModel>(view) {

    lateinit var item: TransactionItemModel
    lateinit var onClick: (item: TransactionItemModel, action: String) -> Unit

    private val itemLayout = itemView.findViewById<ConstraintLayout>(R.id.clItemLayout)
    private val skuValue = itemView.findViewById<TextView>(R.id.tvTxSkuValue)
    private val amountOriginal = itemView.findViewById<TextView>(R.id.tvAmountOriginal)
    private val amountEuro = itemView.findViewById<TextView>(R.id.tvAmountEuro)

    override fun bind(
        item: TransactionItemModel,
        position: Int,
        onClick: (ItemModel, String) -> Unit
    ) {
        this.item = item
        this.onClick = onClick

        val clItemLayout = itemLayout
        val tvSkuValue = skuValue
        val tvAmountOriginal = amountOriginal
        val tvAmountEuro = amountEuro

        clItemLayout.setOneOffClickListener {
            this.onClick.invoke(item, "no_action")
        }
    }
}