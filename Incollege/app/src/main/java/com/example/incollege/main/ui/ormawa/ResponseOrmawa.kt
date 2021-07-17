package com.example.incollege.main.ui.ormawa

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseOrmawa {
    @SerializedName("data")
    @Expose
    val data= mutableListOf<DataClassOrmawa>()

}