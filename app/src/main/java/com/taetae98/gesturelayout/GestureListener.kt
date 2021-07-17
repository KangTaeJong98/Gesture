package com.taetae98.gesturelayout

import android.view.MotionEvent
import android.view.View

class GestureListener(
    private val dragDetector: DragDetector? = DragDetector(),
    private val scaleDetector: ScaleDetector? = ScaleDetector()
) : View.OnTouchListener {
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
                if (mode == Mode.DRAG) {
                    dragDetector?.onActionMove(view, event)
                } else if (mode == Mode.SCALE) {
                    scaleDetector?.onActionMove(view, event)
                }
            }
        }

        return true
    }


    private fun onActionDown(view: View, event: MotionEvent) {
        mode = Mode.DRAG
        dragDetector?.onActionDown(view, event)
    }

    private fun onActionPointerDown(view: View, event: MotionEvent) {
        mode = Mode.SCALE
        dragDetector?.onActionCancel(view, event)
        scaleDetector?.onActionPointerDown(view, event)
    }

    private fun onActionPointerUp(view: View, event: MotionEvent) {
        when (mode) {
            Mode.DRAG -> {
                mode = Mode.NONE
                dragDetector?.onActionCancel(view, event)
            }
            Mode.SCALE -> {
                mode = Mode.DRAG
                dragDetector?.onActionPointerUp(view, event)
                scaleDetector?.onActionPointerUp(view, event)
            }
            else -> {
                mode = Mode.NONE
            }
        }
    }

    private fun onActionCancel(view: View, event: MotionEvent) {
        when (mode) {
            Mode.DRAG -> {
                mode = Mode.NONE
                dragDetector?.onActionCancel(view, event)
            }
            Mode.SCALE -> {
                mode = Mode.DRAG
                scaleDetector?.onActionCancel(view, event)
            }
            else -> {
                mode = Mode.NONE
            }
        }

        mode = Mode.NONE
    }

    enum class Mode {
        NONE, DRAG, SCALE
    }
}