package com.taetae98.gesturelayout.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivityViewModel(
    view: View
) : ViewModel() {
    companion object {
        const val NONE = "NONE"
        const val START = "START"
        const val END = "END"

        const val DRAG = "DRAG"
        const val SCALE = "SCALE"
        const val ROTATE = "ROTATE"
    }

    val translationX by lazy { MutableLiveData(view.translationX) }
    val translationY by lazy { MutableLiveData(view.translationY) }
    val scaleX by lazy { MutableLiveData(view.scaleX) }
    val scaleY by lazy { MutableLiveData(view.scaleY) }
    val rotation by lazy { MutableLiveData(view.rotation) }

    val dragState by lazy { MutableLiveData(NONE) }
    val scaleState by lazy { MutableLiveData(NONE) }
    val rotateState by lazy { MutableLiveData(NONE) }

    class Factory(private val view: View) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainActivityViewModel(view) as T
        }
    }
}