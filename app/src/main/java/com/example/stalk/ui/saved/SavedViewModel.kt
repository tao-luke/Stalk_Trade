package com.example.stalk.ui.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stalk.model.SavedPolitician
import com.example.stalk.util.PreferenceHelper

class SavedViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferenceHelper(application)

    private val _savedPoliticians = MutableLiveData<MutableList<SavedPolitician>>().apply {
        value = prefs.getSavedPoliticians().toMutableList()
    }
    val savedPoliticians: LiveData<MutableList<SavedPolitician>> = _savedPoliticians

    fun addPolitician(politician: SavedPolitician) {
        _savedPoliticians.value?.add(politician)
        _savedPoliticians.value = _savedPoliticians.value // Trigger LiveData update
        prefs.savePoliticians(_savedPoliticians.value!!)
    }

    fun removePolitician(politician: SavedPolitician) {
        _savedPoliticians.value?.remove(politician)
        _savedPoliticians.value = _savedPoliticians.value // Trigger LiveData update
        prefs.savePoliticians(_savedPoliticians.value!!)
    }
}
