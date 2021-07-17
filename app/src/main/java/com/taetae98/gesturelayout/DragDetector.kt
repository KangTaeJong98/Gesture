package com.taetae98.gesturelayout

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View

class DragDetector(
    private val onDragListener: OnDragListener? = GestureDragListener()
) {
    private val basePoint by lazy { PointF() }

    fun onActionDown(view: View, event: MotionEvent) {
        basePoint.set(event.x, event.y)
        onDragListener?.onDragStart(view, event)
    }

    fun onActionPointerUp(view: View, event: MotionEvent) {
        val index = if (event.actionIndex == 0) 1 else 0
        basePoint.set(event.getX(index), event.getY(index))
        onDragListener?.onDragEnd(view, event)
    }

    fun onActionMove(view: View, event: MotionEvent) {
        val distanceX = event.x - basePoint.x
        val distanceY = event.y - basePoint.y

        onDragListener?.onDrag(view, event, distanceX, distanceY)
    }

    interface OnDragListener {
        fun onDragStart(view: View, event: MotionEvent)
        fun onDrag(view: View, event: MotionEvent, distanceX: Float, distanceY: Float)
        fun onDragEnd(view: View, event: MotionEvent)
    }

    class GestureDragListener : OnDragListener {
        override fun onDrag(view: View, event: MotionEvent, distanceX: Float, distanceY: Float) {
            val array = arrayOf(distanceX, distanceY).toFloatArray()

            view.matrix.mapVectors(array)
            view.translationX += array.first()
            view.translationY += array.last()
        }

        override fun onDragStart(view: View, event: MotionEvent) {

        }

        override fun onDragEnd(view: View, event: MotionEvent) {

        }
    }
}