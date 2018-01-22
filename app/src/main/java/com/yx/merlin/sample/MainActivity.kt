package com.yx.merlin.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import merlin.yx.com.detecter.BlockDetectByPrinter
import merlin.yx.com.merlinsample.R

/**
 * Author       : yizhihao (Merlin)
 * Create time  : 2018-01-20 14:09
 * contact      :
 * 562536056@qq.com || yizhihao.hut@gmail.com
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BlockDetectByPrinter.start()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        var textView = findViewById<TextView>(R.id.main_text)
        textView.setText("测试")

        post
    }
}
