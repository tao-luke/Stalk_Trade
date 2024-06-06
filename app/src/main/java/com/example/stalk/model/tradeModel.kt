package com.example.stalk.model
data class Trade(
    val firstName: String = "",
    val lastName: String = "",
    val office: String = "",
    val link: String = "",
    val dateReceived: String = "",
    val transactionDate: String = "",
    val owner: String = "",
    val assetDescription: String = "",
    val assetType: String = "",
    val type: String = "",
    val amount: String = "",
    val comment: String = "",
    val symbol: String = ""
) {
    // No-argument constructor
    constructor() : this("", "", "", "", "", "", "", "", "", "", "", "", "")
}
