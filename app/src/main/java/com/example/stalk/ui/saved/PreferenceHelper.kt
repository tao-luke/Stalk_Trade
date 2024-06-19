package com.example.stalk.ui.saved

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "stalk_prefs"
        private const val KEY_NOTIFICATION_STATE = "notification_state"
    }

    fun setNotificationState(politicianName: String, state: Boolean) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_STATE + politicianName, state).apply()
    }

    fun getNotificationState(politicianName: String): Boolean {
        return preferences.getBoolean(KEY_NOTIFICATION_STATE + politicianName, false)
    }
}
