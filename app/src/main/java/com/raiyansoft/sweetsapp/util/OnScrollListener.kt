package com.raiyansoft.sweetsapp.util

import androidx.recyclerview.widget.RecyclerView

class OnScrollListener(
    val onComplete: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        onComplete()
    }

}