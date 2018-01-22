package com.zihadrizkyef.testingretrofit

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
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


        lyRoot.setOnClickListener {
            val intentTakePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intentTakePhoto, REQUEST_IMAGE_CAPTURE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap

            val a = "zihad"
            val b = encodeImage(imageBitmap).repeat(100)

            val progress = ProgressDialog(this)
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progress.isIndeterminate = false
            progress.progress = 0
            progress.max = 100
            progress.show()

            val body = FormBody.Builder()
                    .add("a", a)
                    .add("b", b)
                    .build()

            val countingBody = CountingRequestBody(body, { bytesWritten, contentLength ->
                progress.progress = (100 * bytesWritten / contentLength).toInt()
                Log.i("aoeu", progress.progress.toString())
            })

            retrofit.upload(countingBody).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_SHORT).show()
                    progress.dismiss()
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    t?.printStackTrace()
                    progress.dismiss()
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
