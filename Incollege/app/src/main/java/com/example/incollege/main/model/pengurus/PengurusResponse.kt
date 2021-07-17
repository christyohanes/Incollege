package com.example.incollege.main.model.pengurus

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PengurusResponse(
    @field:SerializedName("data")
    val data: List<PengurusData?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
) : Parcelable

