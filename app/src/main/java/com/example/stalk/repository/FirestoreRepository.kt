package com.example.stalk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val trades = db.collection("all_trades")
    private val names = db.collection("names")

    fun getTradesByName(firstName: String, lastName: String): Query {
        return trades
            .whereEqualTo("firstName", firstName)
            .whereEqualTo("lastName", lastName)
    }

    fun getRecentTradesByName(firstName: String, lastName: String): Query {
        return trades
            .whereEqualTo("firstName", firstName)
            .whereEqualTo("lastName", lastName)
            .orderBy("transactionDate", Query.Direction.DESCENDING)
    }

    fun getRecentTrades(limit: Long): Query {
        return trades
            .orderBy("transactionDate", Query.Direction.DESCENDING)
            .limit(limit)
    }

    fun getNames(): Query {
        return names
    }

    fun fetchTransactionVolume(firstName: String, lastName: String): LiveData<Int> {
        val volume = MutableLiveData<Int>()
        trades
            .whereEqualTo("firstName", firstName)
            .whereEqualTo("lastName", lastName)
            .get()
            .addOnSuccessListener { result ->
                volume.value = result.size()
            }
        return volume
    }

}
