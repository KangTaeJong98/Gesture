package com.taetae98.gesturelayout.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.taetae98.gesturelayout.binding.DataBinding

abstract class BindingActivity<VB: ViewDataBinding>(
    @LayoutRes
    protected val layoutId: Int
) : AppCompatActivity(), DataBinding<VB> {
    override val binding: VB by lazy { DataBinding.get(this, layoutId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateViewDataBinding()
    }

    protected open fun onCreateViewDataBinding() {
        binding.lifecycleOwner = this
    }
}