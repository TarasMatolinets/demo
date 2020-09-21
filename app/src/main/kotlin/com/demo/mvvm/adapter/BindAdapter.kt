package com.demo.mvvm.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object BindAdapter {

    @JvmStatic
    @BindingAdapter("horizontalLayoutManager")
    fun setRecyclerViewLayoutManager(recyclerView: RecyclerView, isHorizontal: Boolean) {
        if (isHorizontal) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
        }
    }
}
