package com.example.stalk.ui.politician

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PoliticianViewModel : ViewModel() {

    private val _tradeHistory = MutableLiveData<List<TradeHistoryItem>>()
    val tradeHistory: LiveData<List<TradeHistoryItem>> get() = _tradeHistory

    fun updateTradeHistory(newTradeHistory: List<TradeHistoryItem>) {
        _tradeHistory.value = newTradeHistory
    }

    init {
        // Load initial trade history data
        loadTradeHistory()
    }

    private fun loadTradeHistory() {
        // Mock data
        val sampleTradeHistory = listOf(
            TradeHistoryItem("2024-06-01", "Bought 100 shares of XYZ"),
            TradeHistoryItem("2024-05-15", "Sold 50 shares of ABC"),
            // Add more items as needed
        )
        _tradeHistory.value = sampleTradeHistory
    }
}

data class TradeHistoryItem(val date: String, val description: String)
