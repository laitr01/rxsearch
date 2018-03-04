package com.example.macintosh.rxsearch

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by macintosh on 3/3/18.
 */
interface AutoCompleteService {
    @GET("android/jsonarray/") fun autoComplte(): Observable<List<Android>>
}