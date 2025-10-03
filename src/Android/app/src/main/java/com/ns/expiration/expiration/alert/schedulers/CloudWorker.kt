package com.ns.expiration.expiration.alert.schedulers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.ns.expiration.expiration.alert.GoogleSignInClient
import com.ns.expiration.expiration.alert.persistance.entities.AlertWithReminders
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

      // DELETE LOCAL ONES AND FROM THE CLOUD
      val alertsToDelete = localRepository.getAlertsWithReminders(BackupState.PendingDelete)
      alertsToDelete.forEach { alert ->
         try {
            cloudRepository.deleteAlert(alert.alertId(), alert.reminderIds(), alert.imageName())
            localRepository.deleteAlert(alert.alert.id)
         } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
         }
      }

      // UPLOAD LOCAL ONES TO THE CLOUD
      val alertsToUpload = localRepository.getAlertsWithReminders(BackupState.PendingUpload)
      alertsToUpload.forEach { alert ->
         try {
            cloudRepository.uploadAlert(alert.toAlertMap(), alert.toReminderMap(), alert.alert.imageUrl)
            localRepository.updateAlertState(alert.alertId(), BackupState.Uploaded)
         } catch (e: Exception) {
            Firebase.crashlytics.recordException(e)
         }
      }

      return Result.success()
   }
}

private fun AlertWithReminders.toReminderMap(): List<HashMap<String, Any>> {
   return this.reminders.map { reminder ->
      hashMapOf(
         "id" to reminder.id,
         "alertId" to this.alert.id,
         "range" to reminder.range,
         "value" to reminder.value,
         "createdOn" to reminder.createdOn,
      )
   }
}

private fun AlertWithReminders.toAlertMap(): HashMap<String, Any> {
   return hashMapOf(
      "id" to this.alert.id,
      "name" to this.alert.name,
      "quantity" to this.alert.quantity,
      "notes" to this.alert.notes,
      "imageUrl" to this.alert.imageUrl,
      "expirationDate" to this.alert.expirationDate,
      "createdOn" to this.alert.createdOn,
   )
}

private fun AlertWithReminders.reminderIds(): List<String> {
   return this.reminders.map { it.id }
}

private fun AlertWithReminders.imageName(): String {
   return this.alert.imageUrl.split("/").last()
}

private fun AlertWithReminders.alertId(): String {
   return this.alert.id
}