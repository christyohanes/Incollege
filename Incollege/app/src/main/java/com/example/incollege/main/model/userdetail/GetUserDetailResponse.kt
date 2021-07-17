package com.example.incollege.main.model.userdetail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetUserDetailResponse(

    @field:SerializedName("data")
    val data: UserDetailData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
) : Parcelable
