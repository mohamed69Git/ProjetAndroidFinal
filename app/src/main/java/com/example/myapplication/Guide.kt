package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard.main_toolbar
import kotlinx.android.synthetic.main.activity_guide.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

class Guide : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
//        setSupportActionBar(main_toolbar)
        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        main_toolbar.title = ""

        backbutton.setOnClickListener {
            onBackPressed()
        }
    }
}

//fun main(){
//    var a = mutableListOf(2,2)
//    a.add(0,5)
//    for(i in a)
//        println(i)
//
//}