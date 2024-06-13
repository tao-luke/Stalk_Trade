package com.example.stalk.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.example.stalk.model.Name
import com.example.stalk.repository.FirestoreRepository
import android.util.Log

class NameViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _names = MutableLiveData<List<Name>>()
    val names: LiveData<List<Name>> get() = _names

    fun fetchNames() {
        repository.getNames().get()
            .addOnSuccessListener { result ->
                val nameList = mutableListOf<Name>()
                for (document in result) {
                    document.toObject(Name::class.java).let { name ->
                        nameList.add(name)
                    }
                }
                _names.value = nameList
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e("nameViewModel", "Exception occurred: ${e.message}", e)
            }
    }
}
