package com.ns.expiration.expiration.alert.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.ns.expiration.expiration.alert.R

class NotificationController(
   private val context: Context
) {

   companion object {
      const val id: String = "alert_channel"
      const val name: String = "Expiration Alert"
   }

   fun createChannel() {
      val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)

      val notificationManager = context.getSystemService(NotificationManager::class.java)
      notificationManager.createNotificationChannel(channel)
   }

   fun sendNotification(content: String) {
      val notification = NotificationCompat.Builder(context, id)
         .setSmallIcon(R.drawable.ic_alert)
         .setContentTitle(name)
         .setContentText(content)
         .setPriority(NotificationCompat.PRIORITY_MAX)
         .build()

      val notificationManager = context.getSystemService(NotificationManager::class.java)
      notificationManager.notify(1, notification)

   }
}