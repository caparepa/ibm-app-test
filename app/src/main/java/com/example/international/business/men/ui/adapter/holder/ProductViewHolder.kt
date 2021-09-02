package com.example.international.business.men.ui.adapter.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.international.business.men.R
import com.example.international.business.men.ui.adapter.base.DynamicAdapterViewHolder
import com.example.international.business.men.ui.adapter.base.ItemModel
import com.example.international.business.men.ui.adapter.item.model.ProductItemModel
import com.example.international.business.men.utils.setOneOffClickListener

class ProductViewHolder(private val view: View) :
    DynamicAdapterViewHolder<ProductItemModel>(view) {

    lateinit var item: ProductItemModel
    lateinit var onClick: (item: ProductItemModel, action: String) -> Unit

    private val itemLayout = itemView.findViewById<ConstraintLayout>(R.id.clItemLayout)
    private val skuValue = itemView.findViewById<TextView>(R.id.tvSkuValue)
    private val seeDetail = itemView.findViewById<ImageView>(R.id.ivSeeProductDetail)

    override fun bind(item: ProductItemModel, position: Int, onClick: (ItemModel, String) -> Unit) {
        val clItemLayout = itemLayout
        val tvSkuValue = skuValue
        val ivSeeDetail = seeDetail
        this.onClick = onClick

        tvSkuValue.text = item.model.sku

        clItemLayout.setOneOffClickListener {
            this.onClick.invoke(item, "show_toast")
        }

        ivSeeDetail.setOneOffClickListener {
            this.onClick.invoke(item, "go_to_detail")
        }

    }
}