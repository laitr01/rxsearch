package com.example.macintosh.rxsearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var autoSuggestionApdater: AutoSuggestionApdater
    private val autoCompletePublishSubject = PublishSubject.create<String>()
    private lateinit var completeAPI: AutoCompleteAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edit2.requestFocus()

        searchBox.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                if(searchBox.text.toString().isNotEmpty()) {
                    autoCompletePublishSubject.onNext(searchBox.text.toString())
                    recycleView.visibility = View.VISIBLE
                }else {
                    recycleView.visibility = View.GONE
                }
            }

        })
        configureAutoComplte()
        configureAutoComplete()
    }

    private fun configureAutoComplte() {
        recycleView.layoutManager = LinearLayoutManager(this)
        autoSuggestionApdater = AutoSuggestionApdater (immediateSearchListener = {
            onSubmited(it.result)
        })
        recycleView.adapter = autoSuggestionApdater
    }

    private fun onSubmited(input: String) {
        recycleView.visibility = View.GONE
        if(input.isBlank()) return
    }

    private fun configureAutoComplete() {

        completeAPI = AutoCompleteAPI(autoCompleteService = Retrofit.instance().getNewsAPI())

        autoCompletePublishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter { it -> it.isNotEmpty() }
                .switchMap { completeAPI.autoComplete(it).toObservable() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> handleResult(result) }, { t -> t.printStackTrace() })
    }

    private fun handleResult(result: AutoCompleteAPI.AutoCompleteResult) {
        val results = result.suggestions.take(10)
        autoSuggestionApdater.updateData(results)
    }
}


