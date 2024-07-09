package com.example.stalk.ui.saved

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.stalk.R
import com.example.stalk.util.PreferenceHelper

class AlertWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val prefs = PreferenceHelper(applicationContext)
        val savedPoliticians = prefs.getSavedPoliticians()

        val payloadName = inputData.getString("payloadName") ?: return Result.failure()

        if (savedPoliticians.any { it.name == payloadName }) {
            sendNotification()
        }

        return Result.success()
    }

    private fun sendNotification() {
        val channelId = "com.example.stalk.alerts"
        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alert Channel"
            val descriptionText = "Channel for trade alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("New Trade Alert")
            .setContentText("A politician has a new stock trade.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }
}
