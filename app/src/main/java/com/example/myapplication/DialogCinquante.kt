package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.DialogFragment
import com.example.myapplication.service.RetrofitFactory
import kotlinx.android.synthetic.main.ticket_cent.*
import kotlinx.android.synthetic.main.tickets_cinquante.*
import kotlinx.android.synthetic.main.tickets_cinquante.view.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.sql.DriverManager
import java.util.regex.Pattern

class DialogCinquante : DialogFragment() {
    companion object MyConst {
        private var message: String? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.tickets_cinquante, container, true)

        rootView.cancel_cinquante.setOnClickListener {
            dismiss()
        }

        rootView.submitted_cinquante.setOnClickListener {
            if (!Pattern.compile(
                    "([1-9][0-9]*)" // (<digits>)<sdd>*
                ).matcher(edit_ticketfifty.editText?.text.toString().trim()).matches()
            ) {
                edit_ticketfifty.error = "veuillez saisir une quantite valide!!"
                edit_ticketfifty.requestFocus()
                return@setOnClickListener
            }
            buildAchat(Price.PRICE_FIFTY)
        }

        rootView.remove_ticket_cinquante.setOnClickListener {
            if (!Pattern.compile(
                    "([1-9][0-9]*)" // (<digits>)<sdd>*
                ).matcher(edit_ticketfifty.editText?.text.toString().trim()).matches()
            ) {
                edit_ticketfifty.error = "veuillez saisir une quantite valide!!"
                edit_ticketfifty.requestFocus()
                return@setOnClickListener
            }
            buildAchat(Price.NEG_FIFTY)
        }
        return rootView
    }


    fun buildAchat(prix: Int?) {
        progress_indicatorfifty.visibility = VISIBLE

        val sharedPreferences: SharedPreferences? =
            activity?.getSharedPreferences("user_information", Context.MODE_PRIVATE)
        val userid: Int? = sharedPreferences?.getInt("id", 0)
        val quantite: Int? = edit_ticketfifty.editText?.text.toString().toIntOrNull()
        println("Qaunte: $quantite")
            acheter(
                "Bearer " + sharedPreferences!!.getString("token", null).toString(),
                AchatTicket(userid, quantite, prix)
            )
    }

    fun acheter(token: String, achat: AchatTicket) {
        val service = RetrofitFactory.makeRetrofitService()
        val call  = service.achat(token, achat)
        call.enqueue(object : Callback<StringResponse>{
            override fun onResponse(
                call: Call<StringResponse>,
                response: Response<StringResponse>
            ) {
                message = response.body()?.message
                        if (message == "nothing") {
                            edit_ticketfifty.error = "Solde ou nombre de tickets incohérent !!"
                            edit_ticketfifty.requestFocus()
                            progress_indicatorfifty.visibility = GONE
                            return
                        } else {
                            Toast(context).showCustomToast("Ajout ou Retrait effectué effectué",requireActivity())
                            progress_indicatorfifty.visibility = GONE

                        }
                        println(message)
                        println(response.body())
                        DriverManager.println("RESPONSE: yyyyyyyyy ${response.body()?.message}")
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
//                        if (message == "nothing") {
//                            edit_ticketfifty.error = "Solde ou nombre de tickets incohérent !!"
//                            edit_ticketfifty.requestFocus()
//                            return@withContext
//                        } else {
//                            Toast.makeText(context, "Ajout ou Retrait effectué avec succès", Toast.LENGTH_LONG).show()
//                            Handler().postDelayed({
//                                startActivity(Intent(context, Dashboard::class.java))
//                            }, 200)
//                        }
//                        println(message)
//                        println(response.body())
//                        DriverManager.println("RESPONSE: yyyyyyyyy ${response.body()?.message}")
//                    } else {
//                        DriverManager.println("THE RESPONSE FAILED:   ${response.body()}")
//                    }
//                } catch (e: HttpException) {
//                    DriverManager.println("Exception ${e.message}")
//                } catch (e: Throwable) {
//                    DriverManager.println("Oops: Something else went wrong")
//                }
//            }
//        }
    }
}