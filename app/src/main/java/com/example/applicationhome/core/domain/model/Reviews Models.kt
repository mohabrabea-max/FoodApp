package com.example.applicationhome.core.domain.model

data class ReviewsDomainClass(
    val userId : String = "",
    val userName : String = "",
    val createdAt : Long = 0L,
    val resId : Int = 0,
    val stars : Double = 0.0,
    val comment : String = ""
)