package com.example.stalk.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.model.SavedPolitician

class SavedViewModel : ViewModel() {

    private val _savedPoliticians = MutableLiveData<MutableList<SavedPolitician>>().apply {
        value = mutableListOf()
    }
    val savedPoliticians: LiveData<MutableList<SavedPolitician>> = _savedPoliticians

    fun addPolitician(politician: SavedPolitician) {
        _savedPoliticians.value?.add(politician)
        _savedPoliticians.value = _savedPoliticians.value // Trigger LiveData update
    }

    fun removePolitician(politician: SavedPolitician) {
        _savedPoliticians.value?.remove(politician)
        _savedPoliticians.value = _savedPoliticians.value // Trigger LiveData update
    }
}
