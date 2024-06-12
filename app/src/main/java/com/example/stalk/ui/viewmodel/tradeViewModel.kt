package com.example.stalk.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.example.stalk.model.Trade
import com.example.stalk.repository.FirestoreRepository
import android.util.Log

class TradeViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _trades = MutableLiveData<List<Trade>>()
    val trades: LiveData<List<Trade>> get() = _trades

    fun fetchRecentTradesByName(firstName: String, lastName: String, limit: Long) {
        repository.getRecentTradesByName(firstName, lastName, limit).get()
            .addOnSuccessListener { result ->
                val tradeList = mutableListOf<Trade>()
                for (document in result) {
                    document.toObject(Trade::class.java).let { trade ->
                        tradeList.add(trade)
                    }
                }
                _trades.value = tradeList
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e("tradeViewModel", "Exception occurred: ${e.message}", e)
            }
    }

    fun fetchRecentTrades(limit: Long) {
        repository.getRecentTrades(limit).get()
            .addOnSuccessListener { result ->
                val tradeList = mutableListOf<Trade>()
                for (document in result) {
                    document.toObject(Trade::class.java).let { trade ->
                        tradeList.add(trade)
                    }
                }
                _trades.value = tradeList
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e("tradeViewModel", "Exception occurred: ${e.message}", e)
            }
    }
}
