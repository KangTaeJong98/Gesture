package com.taetae98.gesturelayout

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View

class DragDetector(
    private val onDragListener: OnDragListener = GestureDragListener()
) {
    private val basePoint by lazy { PointF() }

    fun onActionDown(event: MotionEvent) {
        basePoint.set(event.x, event.y)
    }

    fun onActionPointerUp(event: MotionEvent) {
        val index = if (event.actionIndex == 0) 1 else 0
        basePoint.set(event.getX(index), event.getY(index))
    }

    fun onDrag(view: View, event: MotionEvent) {
        val distanceX = event.x - basePoint.x
        val distanceY = event.y - basePoint.y

        onDragListener.onDrag(view, distanceX, distanceY)
    }

    interface OnDragListener {
        fun onDrag(view: View, distanceX: Float, distanceY: Float)
    }

    class GestureDragListener : OnDragListener {
        override fun onDrag(view: View, distanceX: Float, distanceY: Float) {
            val array = arrayOf(distanceX, distanceY).toFloatArray()

            view.matrix.mapVectors(array)
            view.translationX += array.first()
            view.translationY += array.last()
        }
    }
}