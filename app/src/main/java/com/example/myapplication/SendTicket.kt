package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.myapplication.service.RetrofitFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.envoyer_ticket.*
import kotlinx.android.synthetic.main.envoyer_ticket.edit_quantite
import kotlinx.android.synthetic.main.envoyer_ticket.view.*
import kotlinx.android.synthetic.main.ticket_cent.*
import kotlinx.android.synthetic.main.tickets_cinquante.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.sql.DriverManager
import java.util.regex.Pattern

class SendTicket : DialogFragment() {
    companion object ConsMess {
        private var message: String? = null
    }

    private var price: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        var rootView: View = inflater.inflate(R.layout.envoyer_ticket, container, false)

        rootView.cancel_send.setOnClickListener {
            dismiss()
        }

        rootView.submitted_send.setOnClickListener {

            val numberAccount = edit_number_account.editText?.text.toString().trim()
            val quantite = edit_quantite.editText?.text.toString().toString().toIntOrNull()
            val selectedId = ticket_radiogroup.checkedRadioButtonId
            val radio = rootView.findViewById<RadioButton>(selectedId)
            if (radio.text.toString() == "ticket de 100f") {
                price = Price.PRICE_CENT
            } else {
                price = Price.PRICE_FIFTY
            }


            if (!Patterns.PHONE.matcher(numberAccount).matches()) {
                edit_number_account.error = "donner un numéro téléphone valable"
                edit_number_account.requestFocus()
                return@setOnClickListener
            }
            if (!Pattern.compile(
                    "([1-9][0-9]*)"
                ).matcher(edit_quantite.editText?.text.toString()).matches()
            ) {
                edit_quantite.error = "veuillez saisir une quantite valide!!"
                edit_quantite.requestFocus()

                return@setOnClickListener
            }

            val sharedPreferences: SharedPreferences? =
                activity?.getSharedPreferences("user_information", Context.MODE_PRIVATE)
            val userid: Int? = sharedPreferences?.getInt("id", 0)
            envoyer(
                "Bearer " + sharedPreferences!!.getString("token", null).toString(),
                DataSendTicket(userid, numberAccount, quantite, price)
            )


        }
        return rootView
    }

    fun envoyer(token: String, data: DataSendTicket) {
        progress_indicator.visibility = VISIBLE

        val service = RetrofitFactory.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.envoyer(token, data)
            withContext(Dispatchers.Main) {
                try {
                    if (response.isSuccessful) {
                        message = response.body()
                        if (message == "Inexistant") {
                            println("message: INEXISTANTINEXISTANTINEXISTANTINEXISTANTINEXISTANTINEXISTANTINEXISTANTINEXISTANT")
                            edit_number_account.error = "Ce numéro téléphone n'est pas reconnu!!"
                            edit_number_account.requestFocus()
                            progress_indicator.visibility = GONE
                            return@withContext
                        } else if (message == "non") {
                            edit_quantite.error = "Impossible d'effectuer cette opération"
                            edit_quantite.requestFocus()
                            progress_indicator.visibility = GONE
                            return@withContext
                        } else {
                            Toast(context).showCustomToast("Envoi effectué avec succès",requireActivity())
                            progress_indicator.visibility = GONE
                        }
                        println("RESPONSE: yyyyyyyyy ${response.body()}")
                    } else {
                        println("GOOOOOOOOOOOOO ${response.body()}  333333")
                        println("THE RESPONSE FAILED:   ${response.body()}")
                    }
                } catch (e: HttpException) {
                    println("GOOOOOOOOOOOOO 444444")
                    DriverManager.println("Exception ${e.message}")
                } catch (e: Throwable) {
                    DriverManager.println("Oops: Something else went wrong")
                }
            }
        }
    }


}