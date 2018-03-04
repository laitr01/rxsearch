package com.example.macintosh.rxsearch

import android.support.annotation.UiThread
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_suggestion.view.*


/**
 * Created by macintosh on 3/3/18.
 */
class AutoSuggestionApdater constructor(private val immediateSearchListener: (AutoCompleteAPI.AutoCompleteSuggestion) -> Unit):
        RecyclerView.Adapter<AutoSuggestionApdater.AutoCompltedViewHolder>() {

    private val suggestions: MutableList<AutoCompleteAPI.AutoCompleteSuggestion>

    override fun getItemCount(): Int {
        if(suggestions.isEmpty()){
            return 1
        }
        return suggestions.size
    }

    override fun onBindViewHolder(holder: AutoCompltedViewHolder, position: Int) {
        when(holder){
            is AutoCompltedViewHolder.SuggestionViewHolder -> {
                val suggestion  = suggestions[position]
                holder.bind(suggestion, immediateSearchListener)
            }
            is AutoCompltedViewHolder.EmptyViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(suggestions.isEmpty()){
            EMPTY_TYPE
        }else{
            SUGGESTION_TYPE
        }
    }
    @UiThread
    fun updateData(newSuggestion: List<AutoCompleteAPI.AutoCompleteSuggestion>){
        suggestions.clear()
        suggestions.addAll(newSuggestion)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoCompltedViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            SUGGESTION_TYPE -> AutoCompltedViewHolder.SuggestionViewHolder(inflater.inflate(R.layout.item_suggestion, parent, false))
            EMPTY_TYPE -> AutoCompltedViewHolder.EmptyViewHolder(inflater.inflate(R.layout.item_suggestion, parent, false))
            else -> throw IllegalArgumentException("Wrong type!!")
        }
    }

    sealed class AutoCompltedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        class SuggestionViewHolder(itemView: View): AutoCompltedViewHolder(itemView){
            fun bind(item: AutoCompleteAPI.AutoCompleteSuggestion,
                     immediateSearchListener: (AutoCompleteAPI.AutoCompleteSuggestion)->Unit) = with(itemView){
                suggestionView.text = item.result
                setOnClickListener { immediateSearchListener(item) }
            }
        }

        class EmptyViewHolder(itemView: View): AutoCompltedViewHolder(itemView){
            fun bind() = with(itemView){
                suggestionView.text = "No have any suggestion."
            }
        }
    }

    companion object {
        private const val EMPTY_TYPE = 1
        private const val SUGGESTION_TYPE = 2
    }

    init {
        this.suggestions = ArrayList()
    }
}