package com.taetae98.modules.gesture.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.setPadding
import com.taetae98.modules.gesture.listener.GestureListener
import com.taetae98.modules.gesture.R


class GestureLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    var childViewPadding = 0
        set(value) {
            field = value
            for (view in children) {
                view.setPadding(value)
            }
        }

    var gestureListener: GestureListener? = GestureListener()
        set(value) {
            field = value
            for (view in children) {
                view.setOnTouchListener(value)
            }
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.GestureLayout, defStyleAttr, defStyleRes).apply {
            childViewPadding = getDimensionPixelSize(R.styleable.GestureLayout_childViewPadding, 0)
        }
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        child.setPadding(childViewPadding)
        child.setOnTouchListener(gestureListener)
    }
}