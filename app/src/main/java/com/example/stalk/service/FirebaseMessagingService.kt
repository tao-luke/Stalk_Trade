package com.example.stalk.service

import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.stalk.ui.saved.AlertWorker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            // Extract the name from the payload
            val payloadName = remoteMessage.data["name"] ?: return

            // Pass the payload to the AlertWorker
            val inputData = Data.Builder()
                .putString("payloadName", payloadName)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<AlertWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(workRequest)
        }
    }

    companion object {
        private const val TAG = "FirebaseMessagingService"
    }
}
