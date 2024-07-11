package com.example.stalk.service

import AlertWorker
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONArray
import org.json.JSONException

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", token)

        // Store the token in Firestore
        storeTokenInFirestore(token)
    }

    private fun storeTokenInFirestore(token: String) {
        val db = FirebaseFirestore.getInstance()

        val tokenMap = hashMapOf(
            "token" to token
        )

        db.collection("fcm_tokens")
            .add(tokenMap)
            .addOnSuccessListener { documentReference ->
                Log.d("Token Stored", "Token stored successfully with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Token Store Failed", "Error storing token", e)
            }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload
        remoteMessage.data.isNotEmpty().let {
//            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            val dataString  = remoteMessage.data["data"]
            // HashMap to store occurrences of full names
            val fullNameCounts = JSONArray(dataString)

            // Iterate through the HashMap and create a WorkRequest for each full name and count
            for (i in 0 until fullNameCounts.length()) {

                val item = fullNameCounts.getJSONObject(i)
                val name = item.getString("name")
                val count = item.getInt("count")

                val inputData = Data.Builder()
                    .putString("fullName", name)
                    .putInt("count", count)
                    .build()

                val workRequest = OneTimeWorkRequestBuilder<AlertWorker>()
                    .setInputData(inputData)
                    .build()

                WorkManager.getInstance(applicationContext).enqueue(workRequest)

                Log.d(TAG, "Enqueued WorkRequest for Full Name: $name, Count: $count")
            }
        }
    }

    companion object {
        private const val TAG = "FirebaseMessagingService"
    }
}
