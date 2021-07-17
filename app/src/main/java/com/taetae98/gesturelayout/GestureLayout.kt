package com.taetae98.gesturelayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.setPadding
import kotlin.properties.Delegates


class GestureLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    var childViewPadding by Delegates.observable(10.toDp()) { _, _, new ->
        for (view in children) {
            view.setPadding(new)
        }
    }

    var listener by Delegates.observable(GestureListener()) { _, _, new ->
        for (view in children) {
            view.setOnTouchListener(new)
        }
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.GestureLayout, defStyleAttr, defStyleRes).apply {
            childViewPadding = getDimensionPixelSize(R.styleable.GestureLayout_childViewPadding, 10.toDp())
        }
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        child.setPadding(childViewPadding)
        child.setOnTouchListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (view in children) {
            val left = ((measuredWidth - view.measuredWidth + 0.5) / 2).toInt() + l
            val right = left + view.measuredWidth
            val top = ((measuredHeight - view.measuredHeight + 0.5) / 2).toInt() + t
            val bottom = top + view.measuredHeight

            view.layout(left, top, right, bottom)
        }
    }

    private fun Int.toDp(): Int {
        return (this * resources.displayMetrics.density + 0.5).toInt()
    }
}