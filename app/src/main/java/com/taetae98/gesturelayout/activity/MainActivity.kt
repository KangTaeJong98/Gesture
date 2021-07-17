package com.taetae98.gesturelayout.activity

import android.os.Bundle
import android.widget.Toast
import com.taetae98.gesturelayout.R
import com.taetae98.gesturelayout.base.BindingActivity
import com.taetae98.gesturelayout.databinding.ActivityMainBinding

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.textView.setOnClickListener {
            val x = it.translationX / binding.parent.measuredWidth * 100
            val y = it.translationY / binding.parent.measuredHeight * 100
            Toast.makeText(this, "$x, $y", Toast.LENGTH_SHORT).show()
        }
    }
}