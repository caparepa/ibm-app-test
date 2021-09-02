package com.example.international.business.men.ui.adapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DynamicAdapterViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T, position: Int, onClick: (ItemModel, String) -> Unit)
}