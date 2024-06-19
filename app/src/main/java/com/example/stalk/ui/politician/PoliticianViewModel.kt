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
        // Check the notification state from SharedPreferences
        politician.isNotified = prefs.getNotificationState(politician.firstName + politician.lastName)
        _politician.value = politician
    }

    fun toggleNotification() {
        _politician.value?.let {
            it.isNotified = !it.isNotified
            prefs.setNotificationState(it.firstName + it.lastName, it.isNotified)
            _politician.value = it
        }
    }
}
