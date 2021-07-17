package com.taetae98.gesturelayout.binding

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

interface DataBinding<VB: ViewDataBinding> {
    val binding: VB

    companion object {
        fun<VB: ViewDataBinding> get(activity: AppCompatActivity, @LayoutRes resId: Int): VB {
            return DataBindingUtil.setContentView(activity, resId)
        }
    }
}