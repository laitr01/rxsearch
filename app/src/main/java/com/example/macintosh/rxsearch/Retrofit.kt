package com.example.macintosh.rxsearch

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



/**
 * Created by macintosh on 3/3/18.
 */
class Retrofit {
    companion object {
        @JvmField
        val BASE_URL = "https://api.learn2crack.com/"
        private var sInstance: com.example.macintosh.rxsearch.Retrofit = com.example.macintosh.rxsearch.Retrofit()
        @JvmStatic
        fun instance(): com.example.macintosh.rxsearch.Retrofit {
            return sInstance
        }
    }

    fun getNewsAPI(): AutoCompleteService {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(AutoCompleteService::class.java)
    }

}