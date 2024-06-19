package com.example.stalk.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.model.Name

class SavedViewModel : ViewModel() {

    private val _savedPoliticians = MutableLiveData<MutableList<Name>>(mutableListOf())
    val savedPoliticians: LiveData<MutableList<Name>> = _savedPoliticians

    fun addPolitician(politician: Name) {
        val currentList = _savedPoliticians.value
        if (currentList?.contains(politician) == false) {
            currentList.add(politician)
            _savedPoliticians.value = currentList
        }
    }

    fun removePolitician(politician: Name) {
        val currentList = _savedPoliticians.value
        if (currentList?.contains(politician) == true) {
            currentList.remove(politician)
            _savedPoliticians.value = currentList
        }
    }
}
