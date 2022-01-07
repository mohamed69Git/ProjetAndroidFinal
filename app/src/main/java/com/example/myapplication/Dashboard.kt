package com.example.myapplication

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.service.RetrofitFactory
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.ticket_cent.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.sql.DriverManager
import android.text.Spannable

import android.text.style.ImageSpan

import android.text.SpannableString

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Dashboard : AppCompatActivity() {
    //    private lateinit var toolBar: Toolbar
//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)
        val sharedPreferences1: SharedPreferences =
            getSharedPreferences("user_information", Context.MODE_PRIVATE)
        val id: Int = sharedPreferences1.getInt("id", 0)
        getCompte("Bearer " + sharedPreferences1.getString("token", null).toString(), id)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        main_toolbar.title = "RestUniv"
        main_toolbar.title
        setSupportActionBar(main_toolbar)

//        history(sharedPreferences1!!.getInt("id", 0),"Bearer " + sharedPreferences1!!.getString("token", null).toString())


        //Wa recyclerView
//        layoutManager = LinearLayoutManager(this)
//        recyclerview.layoutManager = layoutManager
//
//        adapter = RecyclerAdapter()
//        recyclerview.adapter = adapter
        //Wa recyclerView

        val imageButton1: ImageButton = findViewById(R.id.buycent)
        val imageButton2: ImageButton = findViewById(R.id.imageButton2)
        val imageButton3: ImageButton = findViewById(R.id.imageButton3)
//        val imageButton4: ImageButton = findViewById(R.id.imageButton4)

        //variable for my recycler view
        imageButton1.setOnClickListener {
            var dialog = DialogConfirm()
            dialog.show(supportFragmentManager, "cent")
        }

        imageButton2.setOnClickListener {
            var dialog = DialogCinquante()
            dialog.show(supportFragmentManager, "cent")
        }
        imageButton3.setOnClickListener {
            var dialog = SendTicket()
            dialog.show(supportFragmentManager, "send")
        }

        qrcodeGenerator.setOnClickListener {
            startActivity(Intent(this, PhotoQrCode::class.java))
        }
        /*
       * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
       * performs a swipe-to-refresh gesture.
       */
        swiperefresh.setOnRefreshListener {
            Log.i("8888", "onRefresh called from SwipeRefreshLayout")
            getCompte("Bearer " + sharedPreferences1.getString("token", null).toString(), id)
            swiperefresh.isRefreshing = false
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.


        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        menu.add(
            0,
            2,
            2,
            menuIconWithText(
                resources.getDrawable(R.drawable.ic_baseline_help_24),
                resources.getString(R.string.action_about)
            )
        );
        menu.add(
            0,
            3,
            1,
            menuIconWithText(
                resources.getDrawable(R.drawable.ic_baseline_menu_book_24),
                resources.getString(R.string.action_menu)
            )
        );
        menu.add(
            0,
            1,
            3,
            menuIconWithText(
                resources.getDrawable(R.drawable.ic_baseline_power_settings_new_24),
                resources.getString(R.string.action_logout)
            )
        );
        menu.add(
            0,
            4,
            4,
            menuIconWithText(
                resources.getDrawable(R.drawable.ic_baseline_refresh_24),
                resources.getString(R.string.actualiser)
            )
        );
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        val id = item.itemId
        //noinspection SimplifiableIfStatement
        when (item.itemId) {
            1 -> {
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences("user_information", Context.MODE_PRIVATE)
//                sayHello("Bearer "+sharedPreferences.getString("token", null).toString())
                println("The token${sharedPreferences.getString("token", null).toString()}")
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                Toast.makeText(this@Dashboard, "Guenn nga si biti!!", Toast.LENGTH_SHORT)
                    .show()
                editor.clear()
                editor.commit()
                logout("Bearer " + sharedPreferences.getString("token", null).toString())
                startActivity(Intent(this, Login::class.java))
                finish()
                return true
            }
            2 -> {
                Toast.makeText(
                    this@Dashboard,
                    "You need some help",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, Guide::class.java))
                return true
            }
            3 -> {
                Toast.makeText(
                    this@Dashboard,
                    "Help clicked",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, com.example.myapplication.Menu::class.java))
                return true
            }
            // Check if user triggered a refresh:

            4 -> {
                Log.i("false", "Refresh menu item selected")

                // Signal SwipeRefreshLayout to start the progress indicator
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences("user_information", Context.MODE_PRIVATE)
                val idd: Int = sharedPreferences.getInt("id", 0)
                println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%MATAYE ${sharedPreferences.getString("token", null).toString()}")

                swiperefresh.isRefreshing = true
                // Start the refresh background task.
                // This method calls setRefreshing(false) when it's finished.
                getCompte("Bearer " + sharedPreferences.getString("token", null).toString(), idd)
                swiperefresh.isRefreshing = false

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun menuIconWithText(r: Drawable, title: String): CharSequence? {
        r.setBounds(0, 0, r.intrinsicWidth, r.intrinsicHeight)
        val sb = SpannableString("    $title")
        val imageSpan = ImageSpan(r, ImageSpan.ALIGN_BOTTOM)
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return sb
    }

    fun writingHello() {
        print("Hello every one")
        Toast.makeText(this, "yuuuuuuu", Toast.LENGTH_LONG).show()
    }

    fun getCompte(token: String, id: Int) {
        val service = RetrofitFactory.makeRetrofitService()
        val call = service.getAccount(id, token)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("user_information", Context.MODE_PRIVATE)
        call.enqueue(object : Callback<Compte> {
            override fun onResponse(call: Call<Compte>, response: Response<Compte>) {
                breakfast_label.text = response.body()?.nbr_ticket_fifty.toString()
                payment_label.text = response.body()?.solde.toString()
                lunch_label.text = response.body()?.nbr_ticket_cent.toString()
                myName.text = sharedPreferences.getString("name", null)
                if (myName.text == "mor")
                    myiamgeprofile.setImageResource(R.drawable.mor)
                else if (myName.text == "mohamed")
                    myiamgeprofile.setImageResource(R.drawable.myprofile)
                else
                    myiamgeprofile.setImageResource(R.drawable.amath)
                println("Response successful LEMESSAGLEMESSAGLEMESSAGLEMES")
                println("RESPONSE: FROM DASHBOARD ${response.body()}")
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.apply {
                    response.body()?.nbr_ticket_cent?.let { putInt("ticket_cent", it) }
                    response.body()?.solde?.let { putInt("balance", it) }
                    response.body()?.nbr_ticket_fifty?.let { putInt("ticket_fifty", it) }
                }.apply()
            }

            override fun onFailure(call: Call<Compte>, t: Throwable) {
                t.printStackTrace()
            }

        })

        val call2 = service.history(
            sharedPreferences!!.getInt("id", 0),
            "Bearer " + sharedPreferences!!.getString("token", null).toString()
        )
        call2.enqueue(object : Callback<MutableList<History>> {

            override fun onResponse(
                call: Call<MutableList<History>>,
                response: Response<MutableList<History>>
            ) {
                if (response.isSuccessful) {
                    print("8888888888888888888888888888888888888888${response}")
                    recyclerview.apply {
                        layoutManager = LinearLayoutManager(this@Dashboard)
                        adapter = RecyclerAdapter(response.body()!!)
                    }

                }
            }

            override fun onFailure(call: Call<MutableList<History>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }

        })
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = service.getAccount(id, token)
//            withContext(Dispatchers.Main) {
//                try {
//                    if (response.isSuccessful) {
////                        println("Response successful LEMESSAGLEMESSAGLEMESSAGLEMES")
//                        println("RESPONSE: FROM DASHBOARD ${response.body()}")
//                        val sharedPreferences: SharedPreferences =
//                            getSharedPreferences("user_information", Context.MODE_PRIVATE)
//                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//                        editor.apply {
//                            response.body()?.nbr_ticket_cent?.let { putInt("ticket_cent", it) }
//                            response.body()?.solde?.let { putInt("balance", it) }
//                            response.body()?.nbr_ticket_fifty?.let { putInt("ticket_fifty", it) }
//                        }.apply()
//                        breakfast_label.text = response.body()?.nbr_ticket_fifty.toString()
//                        payment_label.text = response.body()?.solde.toString()
//                        lunch_label.text = response.body()?.nbr_ticket_cent.toString()
//                        myName.text = sharedPreferences.getString("name", null)
////                        breakfast_label.text = savedTicketFifty.toString()
//                    } else {
//                        println("THE RESPONSE FAILED:   $response")
//                    }
//                } catch (e: HttpException) {
//                    DriverManager.println("Exception ${e.message}")
//                } catch (e: Throwable) {
//                    DriverManager.println("Oops: Something else went wrong")
//                }
//            }
//        }
    }

    fun logout(string: String) {
        val service = RetrofitFactory.makeRetrofitService()

        Toast.makeText(
            this@Dashboard,
            string, Toast.LENGTH_LONG
        ).show()
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.logout(string)
            withContext(Dispatchers.Main) {
                try {

                    if (response.isSuccessful) {
                        println("RESPONSE: ${response.body()}")
                    } else {
                        print("THE RESPONSE FAILED:   $response")
                    }
                } catch (e: HttpException) {
                    println("Exception ${e.message}")
                } catch (e: Throwable) {
                    println("Oops: Something else went wrong")
                }
            }
        }
    }

    fun sayHello(token: String) {
        val service = RetrofitFactory.makeRetrofitService()
        println(token)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.sayHello(token)
            withContext(Dispatchers.Main) {
                try {

                    if (response.isSuccessful) {
                        println("RESPONSE: ${response.body()}")
                    } else {
                        print("THE RESPONSE FAILED:   $response")
                    }
                } catch (e: HttpException) {
                    println("Exception ${e.message}")
                } catch (e: Throwable) {
                    println("Oops: Something else went wrong")
                }
            }
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
//                        println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`RESPONSE: ${response.body()}")
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


}

