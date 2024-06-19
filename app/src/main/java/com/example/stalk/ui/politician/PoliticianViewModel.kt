package com.example.stalk.ui.politician

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stalk.model.Name
import com.example.stalk.util.PreferenceHelper

class PoliticianViewModel(application: Application) : AndroidViewModel(application) {

    private val _politician = MutableLiveData<Name>()
    val politician: LiveData<Name> get() = _politician

    private val prefs = PreferenceHelper(application)

    fun setPolitician(politician: Name) {
        val fullName = politician.firstName + politician.lastName
        politician.isNotified = prefs.getNotificationState(fullName)
        _politician.value = politician
    }

    fun toggleNotification() {
        _politician.value?.let {
            it.isNotified = !it.isNotified
            val fullName = it.firstName + it.lastName
            prefs.setNotificationState(fullName, it.isNotified)
            _politician.value = it
        }
    }
}
