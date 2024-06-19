package com.example.stalk.util

import android.content.Context
import android.content.SharedPreferences
import com.example.stalk.model.SavedPolitician

class PreferenceHelper(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "stalk_prefs"
        private const val KEY_NOTIFICATION_STATE = "notification_state_"
        private const val KEY_SAVED_POLITICIANS = "saved_politicians"
    }

    fun setNotificationState(politicianName: String, state: Boolean) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_STATE + politicianName, state).apply()
    }

    fun getNotificationState(politicianName: String): Boolean {
        return preferences.getBoolean(KEY_NOTIFICATION_STATE + politicianName, false)
    }

    fun savePoliticians(politicians: List<SavedPolitician>) {
        val serializedList = politicians.joinToString("|") { "${it.name},${it.profilePictureUrl}" }
        preferences.edit().putString(KEY_SAVED_POLITICIANS, serializedList).apply()
    }

    fun getSavedPoliticians(): List<SavedPolitician> {
        val serializedList = preferences.getString(KEY_SAVED_POLITICIANS, null) ?: return emptyList()
        return serializedList.split("|").map {
            val parts = it.split(",")
            SavedPolitician(parts[0], parts[1])
        }
    }
}
