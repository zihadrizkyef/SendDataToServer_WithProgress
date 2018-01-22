package com.zihadrizkyef.testingretrofit

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 20/01/18.
 */
interface API {
    @POST("http://192.168.100.30/aaa.php")
    fun upload(
            @Body b: RequestBody
    ): Call<ResponseBody>
}