package com.taetae98.gesture.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivityViewModel(
    view: View
) : ViewModel() {
    val translationX by lazy { MutableLiveData(view.translationX) }
    val translationY by lazy { MutableLiveData(view.translationY) }
    val scaleX by lazy { MutableLiveData(view.scaleX) }
    val scaleY by lazy { MutableLiveData(view.scaleY) }
    val rotation by lazy { MutableLiveData(view.rotation) }

    val log by lazy { MutableLiveData("") }

    class Factory(private val view: View) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainActivityViewModel(view) as T
        }
    }
}