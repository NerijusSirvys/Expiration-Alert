package com.ns.expiration.expiration.alert.schedulers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.ns.expiration.expiration.alert.extensions.imageUrl
import com.ns.expiration.expiration.alert.extensions.toAlertMap
import com.ns.expiration.expiration.alert.extensions.toAlerts
import com.ns.expiration.expiration.alert.extensions.toReminderMap
import com.ns.expiration.expiration.alert.extensions.toReminders
import com.ns.expiration.expiration.alert.repositories.cloud.AlertOnCloudRepository
import com.ns.expiration.expiration.alert.repositories.local.AlertOnDiskRepository

class CloudWorker(
   context: Context,
   params: WorkerParameters,
   private val cloudRepository: AlertOnCloudRepository,
   private val localRepository: AlertOnDiskRepository,
) : CoroutineWorker(context, params) {

   companion object {
      const val ALERT_ID = "alertId"
      const val USER_ID = "userId"
      const val ACTION = "action"
      const val REMINDER_IDS = "reminderIds"
      const val IMAGE_NAME = "imageName"
      const val DOWNLOAD_COUNT = "downloadCount"
   }

   object Actions {
      enum class Type {
         Save,
         Delete,
         Download
      }
   }

   override suspend fun doWork(): Result {
      val alertId = inputData.getString(ALERT_ID)
      val userId = inputData.getString(USER_ID)
      val actionAsInt = inputData.getInt(ACTION, 0)

      try {
         val action = Actions.Type.entries[actionAsInt]

         when (action) {
            Actions.Type.Save -> {
               val alert = localRepository.getAlertWithReminders(alertId!!)
               cloudRepository.uploadAlert(userId!!, alert.toAlertMap(), alert.toReminderMap(), alert.imageUrl())
            }

            Actions.Type.Delete -> {
               val reminderIds = inputData.getStringArray(REMINDER_IDS)
               val imageName = inputData.getString(IMAGE_NAME)
               cloudRepository.deleteAlert(userId!!, alertId!!, reminderIds!!.toList(), imageName!!)
            }

            Actions.Type.Download -> {
               var counter = 0
               val alertMaps = cloudRepository.downloadAlerts(userId!!)
               val alerts = alertMaps.toAlerts()
               alerts?.forEach { alert ->
                  counter++
                  cloudRepository.downloadAlertImage(userId, alert.imageUrl, alert.id)

                  // add the delay for better image loading. without it you get either partial image loaded or none
                  val reminderMaps = cloudRepository.downloadReminders(userId)
                  val reminders = reminderMaps.toReminders()

                  println("Download Counter: $counter")
                  localRepository.saveAlert(alert, reminders)
               }

               val workData = workDataOf(DOWNLOAD_COUNT to alertMaps?.size)
               return Result.success(workData)
            }
         }
      } catch (e: Exception) {
         Firebase.crashlytics.recordException(e)
         return Result.retry()
      }

      return Result.success()
   }
}