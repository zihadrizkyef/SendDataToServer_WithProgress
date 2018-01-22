package com.zihadrizkyef.testingretrofit

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 20/01/18.
 */
interface API {
    @FormUrlEncoded
    @POST("http://192.168.100.30/aaa.php")
    fun upload(
            @Field("a") a: String,
            @Field("b") b: RequestBody
    ): Call<String>
}