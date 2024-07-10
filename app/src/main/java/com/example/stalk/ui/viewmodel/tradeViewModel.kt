package com.example.stalk.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.example.stalk.model.Trade
import com.example.stalk.repository.FirestoreRepository
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class TradeViewModel : ViewModel() {

    private val repository = FirestoreRepository()

    private val _trades = MutableLiveData<List<Trade>>()
    val trades: LiveData<List<Trade>> get() = _trades

    private val _overviewTrades = MutableLiveData<List<Trade>>()
    val overviewTrades: LiveData<List<Trade>> get() = _overviewTrades

    private val db = FirebaseFirestore.getInstance()

    fun fetchRecentTradesByName(firstName: String, lastName: String) {
        repository.getRecentTradesByName(firstName, lastName).get()
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
                _overviewTrades.value = tradeList // Updating the overviewTrades LiveData
            }
            .addOnFailureListener { e ->
                // Handle the error
                Log.e("tradeViewModel", "Exception occurred: ${e.message}", e)
            }
    }

    fun getTransactionVolume(firstName: String, lastName: String): LiveData<Int> {
        val volume = MutableLiveData<Int>()
        db.collection("all_trades")
            .whereEqualTo("firstName", firstName)
            .whereEqualTo("lastName", lastName)
            .get()
            .addOnSuccessListener { result ->
                volume.value = result.size()
            }
        return volume
    }
}
