package com.example.incollege.main.ui.profile.description

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.incollege.R
import com.example.incollege.main.model.userdetail.GetUserDetailResponse
import com.example.incollege.main.model.userdetail.UserDetailData
import com.example.incollege.main.model.userphoto.EditPhotoResponse
import com.example.incollege.main.network.api.ApiClient
import com.example.incollege.main.network.api.ApiInterface
import com.example.incollege.main.sharedpreferences.SessionManager
import kotlinx.android.synthetic.main.activity_description.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class DescriptionActivity : AppCompatActivity(), View.OnClickListener {

    private var textDescription: String? = null
    private var userDetailData: UserDetailData? = null
    private var loginData: HashMap<String, String>? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        initiateData()
    }

    private fun inititateUI() {
        btn_save_desc.setOnClickListener(this)
        btn_back_desc.setOnClickListener { onBackPressed() }
        et_description_desc.setText(userDetailData?.deskripsi)
    }

    private fun initiateData() {
        loginData = SessionManager(this).getUserData()
        userId = loginData?.get("userid")
        getUserDetail(loginData?.get("userid"))
    }

    private fun getUserDetail(userId: String?) {
        val apiInterface: ApiInterface = ApiClient.instance
        val userDetailCall = apiInterface.getUserDetail(userId)
        userDetailCall.enqueue(object : Callback<GetUserDetailResponse> {
            override fun onResponse(
                call: Call<GetUserDetailResponse>,
                response: Response<GetUserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    userDetailData = response.body()?.data
                    inititateUI()
                } else {
                    try {
                        val responseError =
                            JSONObject(
                                response.errorBody()?.charStream()?.readText() ?: "Error"
                            )
                        Toast.makeText(
                            this@DescriptionActivity,
                            responseError.getString("message"),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } catch (e: IOException) {
                        Toast.makeText(this@DescriptionActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<GetUserDetailResponse>, t: Throwable) {
                Toast.makeText(this@DescriptionActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setDescription(textDesc: String?) {
        val apiInterface: ApiInterface = ApiClient.instance
        val userDescCall =
            apiInterface.setUserDVM(userId, textDesc, userDetailData?.visi, userDetailData?.misi)
        userDescCall.enqueue(object : Callback<EditPhotoResponse> {
            override fun onResponse(
                call: Call<EditPhotoResponse>,
                response: Response<EditPhotoResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@DescriptionActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val responseError =
                        JSONObject(
                            response.errorBody()?.charStream()?.readText() ?: "Error"
                        )
                    Toast.makeText(
                        this@DescriptionActivity,
                        responseError.getString("message"),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }

            override fun onFailure(call: Call<EditPhotoResponse>, t: Throwable) {
                Toast.makeText(this@DescriptionActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save_desc -> {
                textDescription = et_description_desc.text.toString().trim()
                setDescription(textDescription)
            }
        }
    }
}