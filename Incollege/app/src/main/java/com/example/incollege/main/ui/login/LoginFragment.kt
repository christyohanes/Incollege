package com.example.incollege.main.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.incollege.R
import com.example.incollege.main.model.login.LoginData
import com.example.incollege.main.model.login.LoginResponse
import com.example.incollege.main.network.api.ApiClient
import com.example.incollege.main.network.api.ApiInterface
import com.example.incollege.main.sharedpreferences.SessionManager
import com.example.incollege.main.ui.forgetpassword.ForgetPasswordActivity
import com.example.incollege.main.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment(), View.OnClickListener {

    private var isClick: Boolean? = false
    private var textEmail: String? = ""
    private var textPassword: String? = ""
    private lateinit var apiInterface: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateUI()
        checkIsLoggedIn()
    }

    private fun initiateUI() {
        btn_forget_password.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        btn_toggle_password_login.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_forget_password -> startActivity(
                Intent(
                    activity,
                    ForgetPasswordActivity::class.java
                )
            )
            R.id.btn_toggle_password_login -> {
                isClick = if (isClick == false) {
                    et_password_login.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    true
                } else {
                    et_password_login.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    false
                }
            }
            R.id.btn_login -> {
                textEmail = et_email_login.text.toString().toLowerCase().trim()
                textPassword = et_password_login.text.toString().trim()
                if (TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(
                        activity,
                        getString(R.string.failed_btn_save_login_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(
                        activity,
                        getString(R.string.failed_btn_save_login_email_false),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (textPassword?.length!! < 8) {
                    Toast.makeText(
                        activity,
                        getString(R.string.failed_btn_save_pass_below_8),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loginUser(textEmail, textPassword)
                }
            }
        }
    }

    private fun loginUser(textEmail: String?, textPassword: String?) {
        pb_login.visibility = View.VISIBLE
        apiInterface = ApiClient.instance
        val loginCall = apiInterface.loginClient(textEmail, textPassword)
        loginCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    pb_login.visibility = View.GONE
                    val sessionManager = activity?.let { SessionManager(it) }
                    val userData: LoginData? = response.body()?.data
                    if (userData != null) {
                        sessionManager?.createLoginSession(userData)
                    }
                    intentToProfile()
                    Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        pb_login.visibility = View.GONE
                        val responseError =
                            JSONObject(response.errorBody()!!.charStream().readText())
                        Toast.makeText(
                            activity,
                            responseError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                        pb_login.visibility = View.GONE
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                pb_login.visibility = View.GONE
            }
        })
    }

    private fun checkIsLoggedIn() {
        val sessionManager = activity?.let { SessionManager(it) }
        if (sessionManager != null) {
            if (sessionManager.isLoggedIn()) {
                intentToProfile()
            }
        }
    }

    private fun intentToProfile() {
        val profileFragment = ProfileFragment()
        val mFragmentManager = parentFragmentManager
        for (i in 0 until mFragmentManager.backStackEntryCount) {
            mFragmentManager.popBackStack()
        }
        mFragmentManager.beginTransaction().apply {
            replace(
                R.id.navHostFragmentHomePage,
                profileFragment,
                ProfileFragment::class.java.simpleName
            )
            addToBackStack(null)
            commit()
        }
    }

}