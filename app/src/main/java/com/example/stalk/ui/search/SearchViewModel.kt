package com.example.stalk.ui.search

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.ui.viewmodel.TradeViewModel

class SearchViewModel : ViewModel() {
    private val _text = MutableLiveData<String>()

    val text: LiveData<String> get() = _text

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    fun updateText(newText: String) {
        _text.value = newText
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}