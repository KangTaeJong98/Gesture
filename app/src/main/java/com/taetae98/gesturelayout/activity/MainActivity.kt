package com.taetae98.gesturelayout.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.taetae98.gesturelayout.DragDetector
import com.taetae98.gesturelayout.GestureListener
import com.taetae98.gesturelayout.R
import com.taetae98.gesturelayout.ScaleDetector
import com.taetae98.gesturelayout.base.BindingActivity
import com.taetae98.gesturelayout.databinding.ActivityMainBinding
import com.taetae98.gesturelayout.viewmodel.MainActivityViewModel

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val viewModel by viewModels<MainActivityViewModel> { MainActivityViewModel.Factory(binding.textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateOnGestureListener()
        onCreateTextViewOnClickListener()
    }

    override fun onCreateViewDataBinding() {
        super.onCreateViewDataBinding()
        binding.viewModel = viewModel
    }

    private fun onCreateOnGestureListener() {
        binding.parent.listener = GestureListener(
            DragDetector(
                object : DragDetector.GestureDragListener() {
                    override fun onDrag(view: View, event: MotionEvent, distanceX: Float, distanceY: Float) {
                        super.onDrag(view, event, distanceX, distanceY)
                        viewModel.translationX.value = view.translationX
                        viewModel.translationY.value = view.translationY
                        viewModel.dragState.value = MainActivityViewModel.DRAG
                    }

                    override fun onDragStart(view: View, event: MotionEvent) {
                        super.onDragStart(view, event)
                        viewModel.dragState.value = MainActivityViewModel.START
                    }

                    override fun onDragEnd(view: View, event: MotionEvent) {
                        super.onDragEnd(view, event)
                        viewModel.dragState.value = MainActivityViewModel.END
                    }
                }
            ),
            ScaleDetector(
                object : ScaleDetector.GestureScaleListener() {
                    override fun onScaleStart(view: View, event: MotionEvent) {
                        super.onScaleStart(view, event)
                        viewModel.scaleState.value = MainActivityViewModel.START
                    }

                    override fun onScale(view: View, event: MotionEvent, scale: Float) {
                        super.onScale(view, event, scale)
                        viewModel.scaleX.value = view.scaleX
                        viewModel.scaleY.value = view.scaleY
                        viewModel.scaleState.value = MainActivityViewModel.SCALE
                    }

                    override fun onScaleEnd(view: View, event: MotionEvent) {
                        super.onScaleEnd(view, event)
                        viewModel.scaleState.value = MainActivityViewModel.END
                    }
                },
                object : ScaleDetector.GestureRotateListener() {
                    override fun onRotateStart(view: View, event: MotionEvent) {
                        super.onRotateStart(view, event)
                        viewModel.rotateState.value = MainActivityViewModel.START
                    }

                    override fun onRotate(view: View, event: MotionEvent, rotation: Float) {
                        super.onRotate(view, event, rotation)
                        viewModel.rotation.value = view.rotation
                        viewModel.rotateState.value = MainActivityViewModel.ROTATE
                    }

                    override fun onRotateEnd(view: View, event: MotionEvent) {
                        super.onRotateEnd(view, event)
                        viewModel.rotateState.value = MainActivityViewModel.END
                    }
                }
            )
        )
    }

    private fun onCreateTextViewOnClickListener() {
        binding.textView.setOnClickListener {
            val x = it.translationX / binding.parent.measuredWidth * 100
            val y = it.translationY / binding.parent.measuredHeight * 100
            Toast.makeText(this, "$x, $y", Toast.LENGTH_SHORT).show()
        }
    }
}