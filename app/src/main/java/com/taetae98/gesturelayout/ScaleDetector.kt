package com.taetae98.gesturelayout

import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class ScaleDetector(
    private val onScaleListener: OnScaleListener = GestureScaleListener(),
    private val onRotateListener: OnRotateListener = GestureRotateListener()
) {
    private val baseVector by lazy { Vector() }
    private var baseDistance = 0F

    fun onActionPointerDown(event: MotionEvent) {
        baseVector.set(event)
        baseDistance = distance(event)
    }

    fun onScale(view: View, event: MotionEvent) {
        onScaleListener.onScale(view, distance(event) / baseDistance)
        onRotateListener.onRotate(view, view.rotation + Vector.getDegree(baseVector, Vector(event)))
    }

    private fun distance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)

        return sqrt(x*x + y*y)
    }

    interface OnScaleListener {
        fun onScale(view: View, scale: Float)
    }

    interface OnRotateListener {
        fun onRotate(view: View, rotation: Float)
    }

    class GestureRotateListener : OnRotateListener {
        override fun onRotate(view: View, rotation: Float) {
            view.rotation = rotation
        }
    }

    class GestureScaleListener : OnScaleListener {
        override fun onScale(view: View, scale: Float) {
            view.scaleX *= scale
            view.scaleY *= scale
        }
    }

    private class Vector(
        private var x: Float = 0F,
        private var y: Float = 0F
    ) {
        companion object {
            fun getDegree(v1: Vector, v2: Vector): Float {
                return (180.0 / PI * (atan2(v2.y, v2.x) - atan2(v1.y, v1.x))).toFloat()
            }
        }

        constructor(event: MotionEvent) : this() {
            set(event)
        }

        fun set(event: MotionEvent) {
            x = event.getX(1) - event.getX(0)
            y = event.getY(1) - event.getY(0)
            reduction()
        }

        private fun reduction() {
            sqrt(x*x + y*y).also {
                x /= it
                y /= it
            }
        }
    }
}