package com.example.incollege.main.model.userphoto

import com.google.gson.annotations.SerializedName

data class EditPhotoResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
