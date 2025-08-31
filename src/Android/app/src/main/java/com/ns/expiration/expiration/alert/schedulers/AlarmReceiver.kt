package com.ns.expiration.expiration.alert.schedulers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ns.expiration.expiration.alert.notifications.NotificationController
import org.koin.java.KoinJavaComponent.inject

class AlarmReceiver : BroadcastReceiver() {
   override fun onReceive(context: Context?, intent: Intent?) {
      if (context == null) return

      val notificationController by inject<NotificationController>(NotificationController::class.java)
      notificationController.sendNotification("Test")

      val scheduler by inject<AlarmScheduler>(AlarmScheduler::class.java)
      scheduler.setAlarm()
   }
}