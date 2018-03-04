package com.example.macintosh.rxsearch;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by macintosh on 3/4/18.
 */

public interface RequestInterface {
    @GET("android/jsonarray/")
    Observable<List<Android>> register();
}
