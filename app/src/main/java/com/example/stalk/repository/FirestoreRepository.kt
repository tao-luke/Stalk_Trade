package com.example.stalk.repository

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

    fun getRecentTradesByName(firstName: String, lastName: String, limit: Long): Query {
        return trades
            .whereEqualTo("firstName", firstName)
            .whereEqualTo("lastName", lastName)
            .orderBy("transactionDate", Query.Direction.DESCENDING)
            .limit(limit)
    }

    fun getRecentTrades(limit: Long): Query {
        return trades
            .orderBy("transactionDate", Query.Direction.DESCENDING)
            .limit(limit)
    }

    fun getNames(): Query {
        return names
    }

}
