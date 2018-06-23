package utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ItemOffsetDecoration(private val spacing: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect?.set(spacing, spacing, spacing, spacing)
    }
}