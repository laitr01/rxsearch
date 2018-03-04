package com.example.macintosh.rxsearch

import io.reactivex.Observable
import io.reactivex.Single


/**
 * Created by macintosh on 3/3/18.
 */
open class AutoCompleteAPI constructor(private val autoCompleteService: AutoCompleteService){

    fun autoComplete(query: String): Single<AutoCompleteAPI.AutoCompleteResult>{
        if(query.isBlank()){
            return Single.just(AutoCompleteResult(query, emptyList()))
        }
        return autoCompleteService.autoComplte()
                .flatMapIterable { it -> it }
                .map { AutoCompleteSuggestion(it.name) }
                .toList()
                .onErrorReturn { emptyList() }
                .map { AutoCompleteResult(query = query, suggestions = it) }
    }

    data class AutoCompleteResult(val query: String, val suggestions: List<AutoCompleteSuggestion>)

    data class AutoCompleteSuggestion(val result: String)
}