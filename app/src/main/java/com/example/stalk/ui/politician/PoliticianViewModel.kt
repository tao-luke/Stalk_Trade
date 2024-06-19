package com.example.stalk.ui.politician

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.model.Name

class PoliticianViewModel : ViewModel() {

    private val _politician = MutableLiveData<Name>()
    val politician: LiveData<Name> get() = _politician

    fun setPolitician(politician: Name) {
        _politician.value = politician
    }

    fun toggleNotification() {
        _politician.value?.let {
            it.isNotified = !it.isNotified
            _politician.value = it
        }
    }
}
