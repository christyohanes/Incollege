package com.example.incollege.main.model.pengurus

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PengurusData(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("jabatan")
	val jabatan: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("tipe_jabatan")
	val tipeJabatan: String? = null
) : Parcelable
