package com.ns.expiration.expiration.alert.schedulers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.ns.expiration.expiration.alert.GoogleSignInClient
import com.ns.expiration.expiration.alert.extensions.alertId
import com.ns.expiration.expiration.alert.extensions.imageName
import com.ns.expiration.expiration.alert.extensions.imageUrl
import com.ns.expiration.expiration.alert.extensions.reminderIds
import com.ns.expiration.expiration.alert.extensions.toAlertMap
import com.ns.expiration.expiration.alert.extensions.toReminderMap
import com.ns.expiration.expiration.alert.repositories.cloud.AlertOnCloudRepository
import com.ns.expiration.expiration.alert.repositories.local.AlertOnDiskRepository
import com.ns.expiration.expiration.alert.repositories.local.data.BackupState

class CloudWorker(
   context: Context,
   params: WorkerParameters,
   private val localRepository: AlertOnDiskRepository,
   private val cloudRepository: AlertOnCloudRepository,
   private val googleClient: GoogleSignInClient
) : CoroutineWorker(context, params) {
   override suspend fun doWork(): Result {

      if (!googleClient.signedIn())
         return Result.retry()

      val userId = googleClient.getUserId()

      // DELETE LOCAL ONES AND FROM THE CLOUD
      val alertsToDelete = localRepository.getAlertsWithReminders(BackupState.PendingDelete)
      alertsToDelete.forEach { alert ->
         try {
            cloudRepository.deleteAlert(userId, alert.alertId(), alert.reminderIds(), alert.imageName())
            localRepository.deleteAlert(alert.alert.id)
         } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
         }
      }

      // UPLOAD LOCAL ONES TO THE CLOUD
      val alertsToUpload = localRepository.getAlertsWithReminders(BackupState.PendingUpload)
      alertsToUpload.forEach { alert ->
         try {
            cloudRepository.uploadAlert(userId, alert.toAlertMap(), alert.toReminderMap(), alert.imageUrl())
            localRepository.updateAlertState(alert.alertId(), BackupState.Uploaded)
         } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
         }
      }

      return Result.success()
   }
}