package com.example.incollege.main.model.userdetail

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetailData(

	@field:SerializedName("misi")
	val misi: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("visi")
	val visi: String? = null
) : Parcelable
