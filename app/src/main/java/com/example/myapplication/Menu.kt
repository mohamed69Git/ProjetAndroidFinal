package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_guide.*
import kotlinx.android.synthetic.main.activity_guide.backbutton
import kotlinx.android.synthetic.main.activity_menu.*

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        main_toolbar.title = ""

        backbutton2.setOnClickListener {
            onBackPressed()
        }

    }
}