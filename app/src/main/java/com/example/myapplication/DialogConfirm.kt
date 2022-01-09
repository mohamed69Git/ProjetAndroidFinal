package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.DialogFragment
import com.example.myapplication.service.RetrofitFactory
import kotlinx.android.synthetic.main.ticket_cent.*
import kotlinx.android.synthetic.main.ticket_cent.view.*
import kotlinx.android.synthetic.main.tickets_cinquante.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.sql.DriverManager
import java.util.regex.Pattern


class DialogConfirm : DialogFragment() {
    companion object Const {

        private var message: String? = null

    }

    //    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootViewCent: View = inflater.inflate(R.layout.ticket_cent, container, true)

        rootViewCent.cancel_cent.setOnClickListener {
            dismiss()
        }

        //---------------------------------------------

        rootViewCent.submitted_cent.setOnClickListener {

            if (!Pattern.compile(
                    "([1-9][0-9]*)"
                ).matcher(edit_ticketcent.editText?.text.toString()).matches()
            ) {
                edit_ticketcent.error = "veuillez saisir une quantite valide!!"
                edit_ticketcent.requestFocus()
                return@setOnClickListener
            }
            buildAchat(Price.PRICE_CENT)

        }
        //--------------------------------
        rootViewCent.remove_ticket_cent.setOnClickListener {
            if (!Pattern.compile(
                    "([1-9][0-9]*)"
                ).matcher(edit_ticketcent.editText?.text.toString()).matches()
            ) {
                edit_ticketcent.error = "Quantite invalide!!"
                edit_ticketcent.requestFocus()
                return@setOnClickListener
            }
            buildAchat(Price.NEG_CENT)
        }
        return rootViewCent
    }


    fun acheter(token: String, achat: AchatTicket) {

        val service = RetrofitFactory.makeRetrofitService()
        val call = service.achat(token, achat)
        call.enqueue(object : Callback<StringResponse> {
            override fun onResponse(
                call: Call<StringResponse>,
                response: Response<StringResponse>
            ) {
                message = response.body()?.message
//
                if (message == "nothing") {
                    edit_ticketcent.error = "solde ou nombre de tickets incohérents!!"
                    edit_ticketcent.requestFocus()
                    progress_indicatorcent.visibility = GONE
                    return
                } else {
                    Toast(context).showCustomToast("Ajout ou Retrait effectué effectué",requireActivity())
                    progress_indicatorcent.visibility = GONE
//                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                println(achat)
            }

            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = service.achat(token, achat)
//            withContext(Dispatchers.Main) {
//                try {
//                    if (response.isSuccessful) {
//                        message = response.body()?.message
//
//                        if (message == "nothing") {
//                            edit_ticketcent.error = "solde ou nombre de tickets incohérent!!"
//                            edit_ticketcent.requestFocus()
//                            return@withContext
//                        } else {
//                            Toast.makeText(context, "Ajout ou Retrait effectué effectué", Toast.LENGTH_LONG).show()
//                            Handler().postDelayed({
//                                startActivity(Intent(context, Dashboard::class.java))
//
//                            }, 200)
//
//                        }
//                        println(achat)
//                    } else {
//                        println("THE RESPONSE FAILED:   ${response.body()}")
//                    }
//                } catch (e: HttpException) {
//                    println("Exception ${e.message}")
//                } catch (e: Throwable) {
//                    println("Oops: Something else went wrong")
//                }
//            }
//        }
    }

    fun buildAchat(prix: Int?) {
        progress_indicatorcent.visibility = VISIBLE
//        activity?.window?.setFlags(
//            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        val sharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences("user_information", Context.MODE_PRIVATE)
        val userid: Int? = sharedPreferences?.getInt("id", 0)
        val quantite: Int? = edit_ticketcent.editText?.text.toString().toIntOrNull()
        runBlocking {
            acheter(
                "Bearer " + sharedPreferences!!.getString("token", null).toString(),
                AchatTicket(userid, quantite, prix)
            )
        }
    }


}

