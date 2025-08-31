package com.ns.expiration.expiration.alert.schedulers

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class AlarmScheduler(
   private val context: Context
) {
   companion object {
      val defaultScheduleTime: LocalTime = LocalTime.of(9, 0, 0)
   }

   fun setImmediate() {
      setAlarm(LocalDateTime.now().plusSeconds(10))
   }

   fun setAlarm(date: LocalDate) {
      setAlarm(LocalDateTime.of(date, defaultScheduleTime))
   }

   @SuppressLint("MissingPermission")
   private fun setAlarm(dateTime: LocalDateTime) {
      val alarmManager = context.getSystemService(AlarmManager::class.java)
      val intent = Intent(context, AlarmReceiver::class.java)

      val runAt = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

      alarmManager.setExactAndAllowWhileIdle(
         AlarmManager.RTC_WAKEUP, runAt,
         PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
         )
      )
   }
}