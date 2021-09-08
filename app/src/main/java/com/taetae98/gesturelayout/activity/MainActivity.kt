package com.taetae98.gesturelayout.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.taetae98.gesturelayout.R
import com.taetae98.gesturelayout.base.BindingActivity
import com.taetae98.gesturelayout.databinding.ActivityMainBinding
import com.taetae98.gesturelayout.viewmodel.MainActivityViewModel
import com.taetae98.module.gesture.GestureListener

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
        val listenenr  = object : GestureListener() {
            override fun onDrag(view: View, event: MotionEvent, distanceX: Float, distanceY: Float) {
                super.onDrag(view, event, distanceX, distanceY)
                viewModel.translationX.value = view.translationX
                viewModel.translationY.value = view.translationY
                viewModel.log.value += "Drag : $distanceX, $distanceY\n"
            }

            override fun onDragStart(view: View, event: MotionEvent) {
                super.onDragStart(view, event)
                viewModel.log.value += "DragStart\n"
            }

            override fun onDragEnd(view: View, event: MotionEvent) {
                super.onDragEnd(view, event)
                viewModel.log.value += "DragEnd\n"
            }

            override fun onZoomStart(view: View, event: MotionEvent) {
                super.onZoomStart(view, event)
                viewModel.log.value += "ZoomStart\n"
            }

            override fun onTouchStart(view: View, event: MotionEvent) {
                super.onTouchStart(view, event)
                viewModel.log.value += "TouchStart\n"
            }

            override fun onTouchEnd(view: View, event: MotionEvent) {
                super.onTouchEnd(view, event)
                viewModel.log.value += "TouchEnd\n"
            }

            override fun onSingleTap(view: View, event: MotionEvent) {
                super.onSingleTap(view, event)
                viewModel.log.value += "SingleTap\n"
            }

            override fun onZoom(view: View, event: MotionEvent, scale: Float) {
                super.onZoom(view, event, scale)
                viewModel.log.value += "Zoom : $scale\n"
            }

            override fun onZoomEnd(view: View, event: MotionEvent) {
                super.onZoomEnd(view, event)
                viewModel.log.value += "ZoomEnd\n"
            }

            override fun onRotateStart(view: View, event: MotionEvent) {
                super.onRotateStart(view, event)
                viewModel.log.value += "RotateStart\n"
            }

            override fun onRotate(view: View, event: MotionEvent, degree: Float) {
                super.onRotate(view, event, degree)
                viewModel.log.value += "Rotate : $degree\n"
            }

            override fun onRotateEnd(view: View, event: MotionEvent) {
                super.onRotateEnd(view, event)
                viewModel.log.value += "RotateEnd\n"
            }
        }

        binding.textView.setOnTouchListener(listenenr)
    }

    private fun onCreateTextViewOnClickListener() {
        binding.textView.setOnClickListener {
            val x = it.translationX / binding.parent.measuredWidth * 100
            val y = it.translationY / binding.parent.measuredHeight * 100
            Toast.makeText(this, "$x, $y", Toast.LENGTH_SHORT).show()
        }
    }
}