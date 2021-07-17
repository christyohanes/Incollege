package com.example.incollege.main.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.incollege.R
import com.example.incollege.main.listener.BottomSheetItemListener
import com.example.incollege.main.model.userdetail.UserDetailData
import com.example.incollege.main.sharedpreferences.SessionManager
import com.example.incollege.main.ui.login.LoginFragment
import com.example.incollege.main.utils.BottomSheetAlertFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener, BottomSheetItemListener {

    private var userDetailData: UserDetailData? = null
    private var loginData: HashMap<String, String>? = null
    private val mainViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateData()
    }

    private fun initiateUI() {
        btn_logout_profile.setOnClickListener(this)
        btn_edit_data_profile.setOnClickListener(this)
        btn_menu_post_profile.setOnClickListener(this)
        btn_see_full_post_profile.setOnClickListener(this)

        Picasso.with(context)
            .load(userDetailData?.photo)
            .fit()
            .centerCrop()
            .into(civ_user_photo_profile)

        Picasso.with(context)
            .load(userDetailData?.photo)
            .fit()
            .centerCrop()
            .into(civ_post_profile)

        tv_user_name_profile.text = loginData?.get("name")
    }

    private fun initiateData() {
        loginData = activity?.let { SessionManager(it) }?.getUserData()
        getUserDetail(loginData?.get("userid"))
    }

    private fun getUserDetail(userId: String?) {
        loading_screen.visibility = View.VISIBLE
        pb_profil.visibility = View.VISIBLE

        if (userId != null) {
            mainViewModel.setUserDetailData(userId)
        }

        mainViewModel.getUserDetailData().observe(viewLifecycleOwner) {
            userDetailData = it
            initiateUI()
        }

        mainViewModel.getErrorUserDetail().observe(viewLifecycleOwner) {
            Toast.makeText(
                activity,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }

        loading_screen.visibility = View.GONE
        pb_profil.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit_data_profile -> startActivity(
                Intent(
                    activity,
                    EditProfileActivity::class.java
                )
            )
            R.id.btn_logout_profile -> {
                confirmLogout()
            }
        }
    }

    private fun confirmLogout() {
        val btsAlert = BottomSheetAlertFragment(this)
        btsAlert.setTitle("Yakin keluar akun?")
        btsAlert.setSubTitle(
            "Akun anda akan keluar dari aplikasi dan harus masuk kembali dengan memasukkan email dan password.",
            ""
        )
        btsAlert.setTextYesBtn("Ya, Saya yakin!")
        btsAlert.setTextNoBtn("Tidak")
        activity?.let { btsAlert.show(it.supportFragmentManager, "Alert") }
    }

    override fun getUserChoice(yes: Boolean) {
        val sessionManager = activity?.let { SessionManager(it) }
        sessionManager?.logoutSession()
        intentToLogin()
    }

    private fun intentToLogin() {
        val loginFragment = LoginFragment()
        val mFragmentManager = parentFragmentManager
        for (i in 0 until mFragmentManager.backStackEntryCount) {
            mFragmentManager.popBackStack()
        }
        mFragmentManager.beginTransaction().apply {
            replace(
                R.id.navHostFragmentHomePage,
                loginFragment,
                LoginFragment::class.java.simpleName
            )
            addToBackStack(null)
            commit()
        }
    }
}

//        val apiInterface: ApiInterface = ApiClient.instance
//        val userDetailCall = apiInterface.getUserDetail(userId)
//        userDetailCall.enqueue(object : Callback<GetUserDetailResponse> {
//            override fun onResponse(
//                call: Call<GetUserDetailResponse>,
//                response: Response<GetUserDetailResponse>
//            ) {
//                if (response.isSuccessful) {
//                    loading_screen.visibility = View.GONE
//                    pb_profil.visibility = View.GONE
//                    userDetailData = response.body()?.data
//
//                    initiateUI()
//                } else {
//                    try {
//                        loading_screen.visibility = View.GONE
//                        pb_profil.visibility = View.GONE
//
//                        val responseError =
//                            JSONObject(response.errorBody()!!.charStream().readText())
//                        Toast.makeText(
//                            activity,
//                            responseError.getString("message"),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } catch (e: Exception) {
//                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
//                        loading_screen.visibility = View.GONE
//                        pb_profil.visibility = View.GONE
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<GetUserDetailResponse>, t: Throwable) {
//                loading_screen.visibility = View.GONE
//                pb_profil.visibility = View.GONE
//                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
//            }
//
//        })


