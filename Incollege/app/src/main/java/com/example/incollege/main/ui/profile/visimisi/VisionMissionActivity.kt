package com.example.incollege.main.ui.profile.visimisi

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
import kotlinx.android.synthetic.main.activity_vision_mission.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class VisionMissionActivity : AppCompatActivity(), View.OnClickListener {

    private var textVisi: String? = null
    private var textMisi: String? = null
    private var userDetailData: UserDetailData? = null
    private var loginData: HashMap<String, String>? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vision_mission)

        initiateData()
    }

    private fun inititateUI() {
        btn_save_vm.setOnClickListener(this)
        btn_back_vm.setOnClickListener { onBackPressed() }

        et_visi_vm.setText(userDetailData?.visi)
        et_misi_vm.setText(userDetailData?.misi)
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
                            this@VisionMissionActivity,
                            responseError.getString("message"),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } catch (e: IOException) {
                        Toast.makeText(this@VisionMissionActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<GetUserDetailResponse>, t: Throwable) {
                Toast.makeText(this@VisionMissionActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setVisiMisi(textVisi: String?, textMisi: String?) {
        val apiInterface: ApiInterface = ApiClient.instance
        val userVMCall =
            apiInterface.setUserDVM(userId, userDetailData?.deskripsi, textVisi, textMisi)
        userVMCall.enqueue(object : Callback<EditPhotoResponse> {
            override fun onResponse(
                call: Call<EditPhotoResponse>,
                response: Response<EditPhotoResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@VisionMissionActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val responseError =
                        JSONObject(
                            response.errorBody()?.charStream()?.readText() ?: "Error"
                        )
                    Toast.makeText(
                        this@VisionMissionActivity,
                        responseError.getString("message"),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }

            override fun onFailure(call: Call<EditPhotoResponse>, t: Throwable) {
                Toast.makeText(this@VisionMissionActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_save_vm -> {
                textVisi = et_visi_vm.text.toString().trim()
                textMisi = et_misi_vm.text.toString().trim()
                setVisiMisi(textVisi, textMisi)
            }
        }
    }
}