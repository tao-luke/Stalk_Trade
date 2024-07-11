import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
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

        val fullName = inputData.getString("fullName") ?: return Result.failure()
        val count = inputData.getInt("count", 0)

        if (savedPoliticians.any { it.name == fullName }) {
            Log.d(TAG, "Saved match found for $fullName, sending notification")
            sendNotification(fullName, count)
        }

        return Result.success()
    }

    private fun sendNotification(fullName: String, count: Int) {
        val channelId = "com.example.stalk.alerts"
        val notificationId = fullName.hashCode()

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

        // Notification details
        val notificationText = if (count == 1) {
            "$fullName has a new trade."
        } else {
            "$fullName has $count new trades."
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("New Trade Alert")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }

    companion object {
        private const val TAG = "AlertWorker"
    }
}
