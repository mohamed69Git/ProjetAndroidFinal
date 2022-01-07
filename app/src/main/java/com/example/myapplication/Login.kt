package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.ConnectionRequest
import android.util.JsonReader
import android.util.Log
import android.util.Patterns
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.service.RetrofitFactory
import com.example.myapplication.service.RetrofitService
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import javax.sql.ConnectionEventListener

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        val image = findViewById<ImageView>(R.id.logo_image)
        val logo = findViewById<TextView>(R.id.logo)
        val slogan = findViewById<TextView>(R.id.logo_view)
        val password = findViewById<TextInputLayout>(R.id.password)
        val email = findViewById<TextInputLayout>(R.id.email_add)
//        val submit = findViewById<Button>(R.id.submit)
        val sloganame = findViewById<TextView>(R.id.slogan_name)

        submit.setOnClickListener {
            val email = email_add.editText?.text.toString().trim()
            val passwordtest = password.editText?.text.toString().trim()

            val userlog: LoginBody = LoginBody(email, passwordtest)

            if (email.isEmpty()) {
                email_add.error = "the email address is required"
                email_add.requestFocus()
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email_add.editText?.text.toString().trim())
                    .matches()
            ) {
                email_add.error = "this email address is not a correct"
                email_add.requestFocus()
                return@setOnClickListener
            }
            if (passwordtest.length < 8) {
                password.error = "the password must be at least 8 characters"
                password.requestFocus()
                return@setOnClickListener
            }
            if (passwordtest.isEmpty()) {
                password.error = "the password is required"
                password.requestFocus()
                return@setOnClickListener
            }

            login(userlog)


        }
    }

//    fun sayHello(){
//        val service = RetrofitFactory.makeRetrofitService()
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = service.sayHello()
//            withContext(Dispatchers.Main) {
//                try {
//
//                    if (response.isSuccessful) {
//                        println("RESPONSE: ${response.body()}")
//                    } else {
//                        print("THE RESPONSE FAILED:   $response")
//                    }
//                } catch (e: HttpException) {
//                    println("Exception ${e.message}")
//                } catch (e: Throwable) {
//                    println("Oops: Something else went wrong")
//                }
//            }
//        }
//    }

    fun login(user: LoginBody) {
        println("ZZZZZZZZZZZZZZZZZZZZZZZZ")
        val service = RetrofitFactory.makeRetrofitService()
        val call = service.login(user)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body()?.token != null) {
                    println("ZZZZZZZZZZZZZZZZZZZZZZZZ")
                    println(response.body()?.token)

                    val sharedPreferences: SharedPreferences =
                        getSharedPreferences("user_information", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()

                    editor.apply {

                        putString("name", response.body()?.user?.name.toString())
                        putString("token", response.body()?.token.toString())
                        response.body()?.user?.id?.let { putInt("id", it) }
                        putString("email", response.body()?.user?.email)
//

                    }.apply()

                    println("SSAGELEMESSAGELEMESSAGE")
                    launchActivity<Dashboard>()
                    finish()
                }
                else{
                    textError.visibility = VISIBLE
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                println("error "+t.message.toString())
            }

        })
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = service.login(user)
//            withContext(Dispatchers.Main) {
//
//                try {
//                    if (response.isSuccessful) {
//                        if(response.body()?.token != null){
//                            println("ZZZZZZZZZZZZZZZZZZZZZZZZ")
//                            println(response.body()?.token)
//
//                            val sharedPreferences: SharedPreferences = getSharedPreferences("user_information", Context.MODE_PRIVATE)
//                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//
//                            editor.apply{
//
//                                putString("name", response.body()?.user?.name.toString())
//                                putString("token", response.body()?.token.toString())
//                                response.body()?.user?.id?.let { putInt("id", it) }
//                                putString("email", response.body()?.user?.email)
////
//
//                            }.apply()
//
//                            println("SSAGELEMESSAGELEMESSAGE")
//                            launchActivity<Dashboard>()
//                            finish()
//                        }
//                        else{
//                            println("Probleme happened today ${response.body()}")
//                            textError.visibility = VISIBLE
//                        }
//                    } else {
//                        print("THE RESPONSE FAILED:   $response")
////                        Toast.makeText(this@Login, "Vous n'êtes pas connecté au serveur", Toast.LENGTH_LONG).show()
////                        startActivity(Intent(this@Login, Login::class.java))
//                        finish()
//                    }
//                } catch (e: HttpException) {
//
//                    e.printStackTrace()
//
//                    println("Exception ${e.message}")
//                } catch (e: Throwable) {
////                    startActivity(Intent(this@Login, Login::class.java))
////                    finish()
//                    println("Oops: Something else went wrong")
//                }
//            }
//        }
    }

    inline fun <reified T : Any> Context.launchActivity(
        noinline modify: Intent.() -> Unit = {}
    ) {
        val intent = Intent(this, T::class.java)
        intent.modify()
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}