package com.ns.expiration.expiration.alert.schedulers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.ns.expiration.expiration.alert.extensions.imageUrl
import com.ns.expiration.expiration.alert.extensions.toAlertMap
import com.ns.expiration.expiration.alert.extensions.toReminderMap
import com.ns.expiration.expiration.alert.repositories.cloud.AlertOnCloudRepository
import com.ns.expiration.expiration.alert.repositories.local.AlertOnDiskRepository
import com.ns.expiration.expiration.alert.schedulers.data.CloudWorkerAction
import com.ns.expiration.expiration.alert.schedulers.data.SchedulerConstants

class CloudWorker(
   context: Context,
   params: WorkerParameters,
   private val cloudRepository: AlertOnCloudRepository,
   private val localRepository: AlertOnDiskRepository
) : CoroutineWorker(context, params) {
   override suspend fun doWork(): Result {
      val alertId = inputData.getString(SchedulerConstants.ALERT_ID)
      val userId = inputData.getString(SchedulerConstants.USER_ID)
      val actionAsInt = inputData.getInt(SchedulerConstants.ACTION, 0)

      try {
         val action = CloudWorkerAction.entries[actionAsInt]
         if (action == CloudWorkerAction.Save) {
            val alert = localRepository.getAlertWithReminders(alertId!!)
            cloudRepository.uploadAlert(userId!!, alert.toAlertMap(), alert.toReminderMap(), alert.imageUrl())
         } else {
            val reminderIds = inputData.getStringArray(SchedulerConstants.REMINDER_IDS)
            val imageName = inputData.getString(SchedulerConstants.IMAGE_NAME)
            cloudRepository.deleteAlert(userId!!, alertId!!, reminderIds!!.toList(), imageName!!)
         }
      } catch (e: Exception) {
         Firebase.crashlytics.recordException(e)
         return Result.retry()
      }

      return Result.success()
   }
}