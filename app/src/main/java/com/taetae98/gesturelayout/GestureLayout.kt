package com.taetae98.gesturelayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.setPadding
import kotlin.properties.Delegates


class GestureLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    var childViewPadding by Delegates.observable(0) { _, _, new ->
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
            childViewPadding = getDimensionPixelSize(R.styleable.GestureLayout_childViewPadding, 0)
        }
    }

    override fun onViewAdded(child: View) {
        super.onViewAdded(child)
        child.setPadding(childViewPadding)
        child.setOnTouchListener(listener)
    }
}