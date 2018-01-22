package com.zihadrizkyef.testingretrofit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import com.android.gotosehat.registrasi_new.RequestBodyWithProgress
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var retrofit: API

    private val REQUEST_IMAGE_CAPTURE: Int = 42424

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl("http://192.168.100.30")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(API::class.java)

        val intentTakePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentTakePhoto, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap

            val a = "zihad"
            val b = encodeImage(imageBitmap) + encodeImage(imageBitmap) + encodeImage(imageBitmap)
            val bBody = RequestBodyWithProgress(MediaType.parse("text/plain; charset=utf-8")!!, b, object : RequestBodyWithProgress.UploadCallbacks {
                override fun onProgressUpdate(percentage: Int) {
                    Log.i("progress", percentage.toString())
                }
            })
            retrofit.upload(a, bBody).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    Log.i("response", response.toString())
                }

                override fun onFailure(call: Call<String>?, t: Throwable?) {
                    t?.printStackTrace()
                }
            })
        }
    }

    private fun encodeImage(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byte = baos.toByteArray()
        return Base64.encodeToString(byte, Base64.DEFAULT)
    }
}
