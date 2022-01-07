package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_photo_qr_code.*
import java.util.Collections.list


class PhotoQrCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_qr_code)

        val sharedPreferences: SharedPreferences = getSharedPreferences("user_information", Context.MODE_PRIVATE)
        print("Voici le contenu de sharedpreferences $sharedPreferences")
        val id: Int? = sharedPreferences.getInt("id",0)
//        val email: String? = sharedPreferences.getString("email", null)



        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode("$id", BarcodeFormat.QR_CODE, 712, 712)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width){
                for(y in 0 until height){
                    bmp.setPixel(x, y, if(bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }

            qrcodeImage.setImageBitmap(bmp)

        }catch (e: WriterException){
            e.printStackTrace()
        }

        image_shutter.setOnClickListener {
            onBackPressed()
        }
    }

}