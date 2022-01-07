package com.example.myapplication

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object Constants {
        private const val SPLASH_SCREEN: Long = 2000
    }

    //Variables
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var image: ImageView
    private lateinit var slogan: TextView
    private lateinit var logo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //removing the actionBar
        window.setFlags(    FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main);
//        textView.text = "Bonjour"
        //Animations
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        //Hooks
        image = findViewById(R.id.imageView)
        logo = findViewById(R.id.textView)
        slogan = findViewById(R.id.textView2)

        image.animation = topAnimation
        logo.animation = bottomAnimation
        slogan.animation = bottomAnimation



        //creating a new intent
        Handler().postDelayed(fun() {

            val intent = Intent(this, Login::class.java)
            var a: View  = logo
            var b: String  = "logo_text"
            var c: View = image
            var d: String = "logo_image"
            var pair1 = Pair(a, b)
            var pair2 = Pair(c, d)


            val options = ActivityOptions.makeSceneTransitionAnimation(this, pair1, pair2)
            val sharedPreferences1: SharedPreferences = getSharedPreferences("user_information", Context.MODE_PRIVATE)
            val token = sharedPreferences1.getString("token", null)
            if(token !="" && token != null){
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            }
            else{
                startActivity(intent, options.toBundle())
                finish()
            }
        }, SPLASH_SCREEN)


    }

}
