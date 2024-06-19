package com.example.stalk.model

data class Name(
    val firstName: String = "",
    val lastName: String = "",
    val img: String = "",
    var isNotified: Boolean = false
) {
    // No-argument constructor
    constructor() : this("", "", "", false)
}
