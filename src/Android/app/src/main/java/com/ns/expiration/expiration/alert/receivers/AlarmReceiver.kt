package com.ns.expiration.expiration.alert.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ns.expiration.expiration.alert.notifications.Notification
import com.ns.expiration.expiration.alert.notifications.NotificationController
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import com.ns.expiration.expiration.alert.schedulers.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

class AlarmReceiver : BroadcastReceiver() {
   private val scope = CoroutineScope(SupervisorJob())

   override fun onReceive(context: Context?, intent: Intent?) {
      if (context == null) return

      val pendingResult = goAsync()

      val repo by KoinJavaComponent.inject<AlertRepository>(AlertRepository::class.java)
      val today = LocalDate.now()

      val notificationController by KoinJavaComponent.inject<NotificationController>(NotificationController::class.java)
      scope.launch {
         val alerts = repo.getActiveAlerts()

         alerts.forEach { (alert, reminders) ->
            reminders.forEach { reminder ->
               val reminderDate = getReminderDate(reminder.value.toLong(), reminder.range, alert.expirationDate)
               val difference = Period.between(today, reminderDate)

               if (difference.isNegative || difference.isZero) {
                  notificationController.sendNotification(Notification.Companion.fromAlert(alert))
               }
            }
         }

         val scheduler by KoinJavaComponent.inject<AlarmScheduler>(AlarmScheduler::class.java)

         val timeNow = LocalTime.now()
         val intendedTime = LocalTime.of(9, 0)

         if (timeNow < intendedTime) {
            scheduler.setAlarm(today)
         } else {
            scheduler.setAlarm(today.plusDays(1))
         }

         pendingResult.finish()
      }
   }

   fun getReminderDate(value: Long, range: ReminderRange, date: LocalDate): LocalDate? {
      return when (range) {
         ReminderRange.Days -> date.minusDays(value)
         ReminderRange.Weeks -> date.minusWeeks(value)
         ReminderRange.Months -> date.minusMonths(value)
      }
   }
}