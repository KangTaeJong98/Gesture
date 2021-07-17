package com.taetae98.gesturelayout

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

open class GestureListener : View.OnTouchListener {
    private val point by lazy { PointF() }
    private val vector by lazy { Vector() }
    private var distance = 0F

    private var mode = Mode.NONE

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(view, event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                onActionPointerDown(view, event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                onActionPointerUp(view, event)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                view.performClick()
                onActionCancel(view, event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == Mode.DRAG || mode == Mode.SCALE) {
                    onActionMove(view, event)
                }
            }
        }

        return true
    }


    private fun onActionDown(view: View, event: MotionEvent) {
        mode = Mode.DRAG
        point.set(event.x, event.y)
        onDragStart(view, event)
    }

    private fun onActionPointerDown(view: View, event: MotionEvent) {
        mode = Mode.SCALE
        vector.set(event)
        distance = distance(event)

        onDragEnd(view, event)
        onScaleStart(view, event)
        onRotateStart(view, event)
    }

    private fun onActionPointerUp(view: View, event: MotionEvent) {
        when (mode) {
            Mode.DRAG -> {
                mode = Mode.NONE
                onDragEnd(view, event)
            }
            Mode.SCALE -> {
                mode = Mode.DRAG
                val index = if (event.actionIndex == 0) 1 else 0
                point.set(event.getX(index), event.getY(index))
                onDragStart(view, event)
                onScaleEnd(view, event)
                onRotateEnd(view, event)
            }
            else -> {
                mode = Mode.NONE
            }
        }
    }

    private fun onActionCancel(view: View, event: MotionEvent) {
        if (mode == Mode.DRAG) {
            onDragEnd(view, event)
        } else if (mode == Mode.SCALE) {
            onScaleEnd(view, event)
            onRotateEnd(view, event)
        }

        mode = Mode.NONE
    }

    private fun onActionMove(view: View, event: MotionEvent) {
        if (mode == Mode.DRAG) {
            onActionDrag(view, event)
        } else if (mode == Mode.SCALE) {
            onActionScale(view, event)
            onActionRotate(view, event)
        }
    }

    private fun onActionDrag(view: View, event: MotionEvent) {
        val distanceX = event.x - point.x
        val distanceY = event.y - point.y

        onDrag(view, event, distanceX, distanceY)
    }

    private fun onActionScale(view: View, event: MotionEvent) {
        onScale(view, event, distance(event) / distance)
    }

    private fun onActionRotate(view: View, event: MotionEvent) {
        onRotate(view, event, view.rotation + Vector.getDegree(vector, Vector(event)))
    }

    private fun distance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)

        return sqrt(x*x + y*y)
    }

    open fun onDrag(view: View, event: MotionEvent, distanceX: Float, distanceY: Float) {
        val array = arrayOf(distanceX, distanceY).toFloatArray()

        view.matrix.mapVectors(array)
        view.translationX += array.first()
        view.translationY += array.last()
    }

    open fun onDragStart(view: View, event: MotionEvent) {

    }

    open fun onDragEnd(view: View, event: MotionEvent) {

    }

    open fun onScaleStart(view: View, event: MotionEvent) {

    }

    open fun onScale(view: View, event: MotionEvent, scale: Float) {
        view.scaleX *= scale
        view.scaleY *= scale
    }

    open fun onScaleEnd(view: View, event: MotionEvent) {

    }

    open fun onRotateStart(view: View, event: MotionEvent) {

    }

    open fun onRotate(view: View, event: MotionEvent, rotation: Float) {
        view.rotation = rotation
    }

    open fun onRotateEnd(view: View, event: MotionEvent) {

    }

    enum class Mode {
        NONE, DRAG, SCALE
    }

    class Vector(x: Float = 0F, y: Float = 0F) : PointF(x, y) {
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