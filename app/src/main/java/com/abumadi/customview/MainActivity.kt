package com.abumadi.customview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.abumadi.customview.Views.CustomView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swapColor.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        customView.swapColor()
    }
}