package com.example.stalk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.math.roundToInt

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

    fun fetchTradeVolume(firstName: String, lastName: String): LiveData<Int> {
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

    fun fetchTransactionVolume(firstName: String, lastName: String): LiveData<Int> {
        val volume = MutableLiveData<Int>()
        trades
            .whereEqualTo("firstName", firstName)
            .whereEqualTo("lastName", lastName)
            .get()
            .addOnSuccessListener { result ->
                val medians = result.mapNotNull { document ->
                    try {
                        val amount = document.getString("amount") ?: return@mapNotNull null
                        val ranges = amount.split(" - ").mapNotNull {
                            it.removePrefix("$").replace(",", "").toDoubleOrNull()
                        }
                        if (ranges.size == 2) {
                            (ranges[0] + ranges[1]) / 2
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null // Skip this trade if any parsing error occurs
                    }
                }
                val totalVolume = if (medians.isNotEmpty()) {
                    medians.sum()
                } else {
                    0.0
                }
                volume.value = totalVolume.roundToInt()
            }
            .addOnFailureListener { e ->
                volume.value = 0 // Set to 0 in case of a failure
            }
        return volume
    }
}
