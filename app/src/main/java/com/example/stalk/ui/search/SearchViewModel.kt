package com.example.stalk.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Search results will appear here"
    }
    val text: LiveData<String> = _text

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun updateText(newText: String) {
        _text.value = newText
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
