package com.example.stalk.model
data class Trade(
    val firstName: String = "",
    val lastName: String = "",
    val link: String = "",
    val dateRecieved: String = "",
    val transactionDate: String = "",
    val owner: String = "",
    val assetDescription: String = "",
    val type: String = "",
    val amount: String = "",
    val ticker: String = ""
) {
    // No-argument constructor
    constructor() : this("", "", "", "", "", "", "", "", "", "")
}
