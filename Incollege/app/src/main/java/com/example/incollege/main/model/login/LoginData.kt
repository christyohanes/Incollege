package com.example.incollege.main.model.login

import com.google.gson.annotations.SerializedName

data class LoginData(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)