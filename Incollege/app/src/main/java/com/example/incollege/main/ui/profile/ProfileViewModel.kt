package com.example.incollege.main.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.incollege.main.model.userdetail.GetUserDetailResponse
import com.example.incollege.main.model.userdetail.UserDetailData
import com.example.incollege.main.network.api.ApiClient
import com.example.incollege.main.network.api.ApiInterface
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ProfileViewModel : ViewModel() {

    private val userDetailData = MutableLiveData<UserDetailData>()
    private var errorMessageUserDetail = MutableLiveData<String>()

    fun setUserDetailData(userId: String) {
        val apiInterface: ApiInterface = ApiClient.instance
        val userDetailCall = apiInterface.getUserDetail(userId)
        userDetailCall.enqueue(object : Callback<GetUserDetailResponse> {
            override fun onResponse(
                call: Call<GetUserDetailResponse>,
                response: Response<GetUserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    userDetailData.postValue(response.body()?.data)
                } else {
                    errorMessageUserDetail.postValue(
                        try {
                            val responseError =
                                JSONObject(
                                    response.errorBody()?.charStream()?.readText() ?: "Error"
                                )
                            responseError.getString("message")
                        } catch (e: IOException) {
                            e.message
                        }
                    )
                }
            }

            override fun onFailure(call: Call<GetUserDetailResponse>, t: Throwable) {
                errorMessageUserDetail.postValue(t.localizedMessage)
            }
        })
    }

    fun getUserDetailData(): LiveData<UserDetailData> {
        return userDetailData
    }

    fun getErrorUserDetail(): LiveData<String> {
        return errorMessageUserDetail
    }
}