package com.example.stalk.model

data class Name(
    val firstName: String = "",
    val lastName: String = ""
)

{
    // No-argument constructor
    constructor() : this("", "")
}