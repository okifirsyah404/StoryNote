package com.okifirsyah.customview.recyclerview.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListRecyclerViewItemDivider(private val gap: Int, private val margin: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = margin
            }

            bottom = if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
                margin
            } else {
                gap
            }
        }
    }
}