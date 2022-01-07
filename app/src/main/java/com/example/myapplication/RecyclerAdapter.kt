package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.service.RetrofitFactory
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAdapter(val history: MutableList<History>):RecyclerView.Adapter<RestEnterHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestEnterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent,false)
        return RestEnterHolder(view)
    }

    override fun onBindViewHolder(holder: RestEnterHolder, position: Int) {
        return holder.bindView(history[position])
    }

    override fun getItemCount(): Int {
        return history.size
    }
}

class RestEnterHolder(itemView: View): RecyclerView.ViewHolder(itemView){
   private val title: TextView = itemView.findViewById(R.id.titleresto)
    private val dateresto: TextView = itemView.findViewById(R.id.dateresto)
    private val imageView: ImageView = itemView.findViewById(R.id.iconshow)

    fun bindView(history: History){
        title.text = history.contexte
        dateresto.text = history.created_at
        if(title.text == "repas" || title.text == "dîner")
            imageView.setImageResource(R.drawable.breakfast_icon)
        else imageView.setImageResource(R.drawable.lunch_icon)

    }
}