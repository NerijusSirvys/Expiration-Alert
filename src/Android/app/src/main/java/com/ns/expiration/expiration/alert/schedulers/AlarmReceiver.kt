package com.ns.expiration.expiration.alert.schedulers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ns.expiration.expiration.alert.notifications.Notification
import com.ns.expiration.expiration.alert.notifications.NotificationController
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.time.LocalDate
import java.time.Period

class AlarmReceiver : BroadcastReceiver() {
   private val scope = CoroutineScope(SupervisorJob())

   override fun onReceive(context: Context?, intent: Intent?) {
      if (context == null) return

      val pendingResult = goAsync()

      val repo by inject<AlertRepository>(AlertRepository::class.java)
      val today = LocalDate.now()

      val notificationController by inject<NotificationController>(NotificationController::class.java)
      scope.launch {
         val alerts = repo.getActiveAlerts()

         alerts.forEach { (alert, reminders) ->
            reminders.forEach { reminder ->
               val reminderDate = getReminderDate(reminder.value.toLong(), reminder.range, alert.expirationDate)
               val difference = Period.between(today, reminderDate)

               if (difference.isNegative || difference.isZero) {
                  notificationController.sendNotification(Notification.fromAlert(alert))
               }
            }
         }

         val scheduler by inject<AlarmScheduler>(AlarmScheduler::class.java)
         scheduler.setAlarm(today.plusDays(1))

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