package com.taetae98.gesturelayout

import android.view.MotionEvent
import android.view.View

class GestureListener(
    private val dragDetector: DragDetector = DragDetector(),
    private val scaleDetector: ScaleDetector = ScaleDetector()
) : View.OnTouchListener {
    private var mode = Mode.NONE

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        view.performClick()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                onActionPointerDown(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                onActionPointerUp(event)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                onActionCancel()
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == Mode.DRAG) {
                    dragDetector.onDrag(view, event)
                } else if (mode == Mode.SCALE) {
                    scaleDetector.onScale(view, event)
                }
            }
        }

        return true
    }

    private fun onActionDown(event: MotionEvent) {
        mode = Mode.DRAG
        dragDetector.onActionDown(event)
    }

    private fun onActionPointerDown(event: MotionEvent) {
        mode = Mode.SCALE
        scaleDetector.onActionPointerDown(event)
    }

    private fun onActionPointerUp(event: MotionEvent) {
        when (mode) {
            Mode.DRAG -> {
                mode = Mode.NONE
            }
            Mode.SCALE -> {
                mode = Mode.DRAG
                dragDetector.onActionPointerUp(event)
            }
            else -> {
                mode = Mode.NONE
            }
        }
    }

    private fun onActionCancel() {
        mode = Mode.NONE
    }

    enum class Mode {
        NONE, DRAG, SCALE
    }
}