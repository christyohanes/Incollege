package com.example.incollege.main.sharedpreferences

import android.content.Context
import com.example.incollege.main.model.login.LoginData

class SessionManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "session_preference"
        private const val IS_LOGGED_IN = "isloggedin"
        private const val USER_ID = "userid"
        private const val EMAIL = "email"
        private const val USER_NAME = "name"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun createLoginSession(user: LoginData) {
        val editor = preferences.edit()
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putString(USER_ID, user.id)
        editor.putString(EMAIL, user.email)
        editor.putString(USER_NAME, user.name)
        editor.apply()
    }

    fun getUserData(): HashMap<String, String> {
        val user = HashMap<String, String>()
        user[USER_ID] = preferences.getString(USER_ID, null).toString()
        user[EMAIL] = preferences.getString(EMAIL, null).toString()
        user[USER_NAME] = preferences.getString(USER_NAME, null).toString()
        return user
    }

    fun logoutSession() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(IS_LOGGED_IN, false)
    }

}