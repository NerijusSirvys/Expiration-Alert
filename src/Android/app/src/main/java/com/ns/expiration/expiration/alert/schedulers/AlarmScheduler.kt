package com.ns.expiration.expiration.alert.schedulers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import org.koin.java.KoinJavaComponent.inject
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmScheduler(
   private val context: Context
) {
   @SuppressLint("MissingPermission")
   fun setAlarm() {
      val alarmManager = context.getSystemService(AlarmManager::class.java)
      val intent = Intent(context, AlarmReceiver::class.java)

//    val tomorrow = LocalDate.now().plusDays(1)
//    val dateTuRun = LocalDateTime.of(tomorrow, LocalTime.of(7, 0))

      val dateTuRun = LocalDateTime.now().plusSeconds(20)

      val dateToRinInMillis = dateTuRun.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

      val repo: AlertRepository by inject(AlertRepository::class.java)

      alarmManager.setExactAndAllowWhileIdle(
         AlarmManager.RTC_WAKEUP, dateToRinInMillis,
         PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
         )
      )

   }
}