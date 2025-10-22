package com.ns.expiration.expiration.alert.services

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.ns.expiration.expiration.alert.GoogleSignInClient
import com.ns.expiration.expiration.alert.extensions.imageName
import com.ns.expiration.expiration.alert.extensions.reminderIds
import com.ns.expiration.expiration.alert.extensions.toAlertEntity
import com.ns.expiration.expiration.alert.extensions.toAlerts
import com.ns.expiration.expiration.alert.extensions.toReminderEntities
import com.ns.expiration.expiration.alert.extensions.toReminders
import com.ns.expiration.expiration.alert.notifications.Notification
import com.ns.expiration.expiration.alert.notifications.NotificationController
import com.ns.expiration.expiration.alert.repositories.cloud.AlertOnCloudRepository
import com.ns.expiration.expiration.alert.repositories.local.AlertOnDiskRepository
import com.ns.expiration.expiration.alert.repositories.local.data.AlertDetails
import com.ns.expiration.expiration.alert.repositories.local.data.AlertOverview
import com.ns.expiration.expiration.alert.repositories.local.data.BackupState
import com.ns.expiration.expiration.alert.schedulers.CloudWorker
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreenState
import com.ns.expiration.expiration.alert.utilities.DateTimeHelpers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AlertService(
   private val localRepo: AlertOnDiskRepository,
   private val cloudRepo: AlertOnCloudRepository,
   private val googleClient: GoogleSignInClient,
   private val workManager: WorkManager,
   private val notificationController: NotificationController,
   private val context: Context
) {

   suspend fun getAlertById(id: String): Flow<AlertDetails> {
      return localRepo.getAlertById(id)
   }

   suspend fun createAlert(id: String, imageUrl: String, request: ManageAlertScreenState) {
      val formatter = DateTimeFormatter.ofPattern(DateTimeHelpers.FORMAT)
      val expirationDate = LocalDate.parse(request.expirationDate.value, formatter)

      val createdOn = Instant.ofEpochMilli(System.currentTimeMillis())
         .atZone(ZoneId.systemDefault())
         .toLocalDateTime()

      // TODO: Remove when multiple expiration dates are implemented
      val dates = listOf(expirationDate, expirationDate)

      val alert = request.toAlertEntity(id, imageUrl, createdOn, dates, BackupState.PendingUpload)
      val reminders = request.toReminderEntities(id, createdOn)
      localRepo.saveAlert(alert, reminders)

      val constraints = getWorkerConstraints()

      val data = Data.Builder()
         .putString(CloudWorker.ALERT_ID, id)
         .putString(CloudWorker.USER_ID, googleClient.getUserId())
         .putInt(CloudWorker.ACTION, CloudWorker.Actions.Type.Save.ordinal)
         .build()

      val workRequest = getWorkRequest(constraints, data)

      workManager.enqueueUniqueWork(id, ExistingWorkPolicy.REPLACE, workRequest)
   }

   suspend fun deleteAlert(id: String) {
      val alert = localRepo.getAlertWithReminders(id)

      val data = Data.Builder()
         .putString(CloudWorker.ALERT_ID, id)
         .putString(CloudWorker.USER_ID, googleClient.getUserId())
         .putInt(CloudWorker.ACTION, CloudWorker.Actions.Type.Delete.ordinal)
         .putStringArray(CloudWorker.REMINDER_IDS, alert.reminderIds().toTypedArray())
         .putString(CloudWorker.IMAGE_NAME, alert.imageName())
         .build()

      val workRequest = getWorkRequest(getWorkerConstraints(), data)
      workManager.enqueueUniqueWork(id, ExistingWorkPolicy.REPLACE, workRequest)

      try {
         localRepo.deleteAlert(id)
         context.deleteFile("${alert.alert.name}_${id}.webp")
      } catch (e: Exception) {
         Firebase.crashlytics.recordException(e)
         throw e
      }
   }

   fun getActiveAlertOverviews(): Flow<List<AlertOverview>> {
      return localRepo.getActiveAlertOverviews()
   }

   suspend fun downloadBackups(onSuccess: () -> Unit, onFailure: () -> Unit) {
      try {
         val userId = googleClient.getUserId()
         if (userId.isEmpty()) return

         val alertMaps = cloudRepo.downloadAlerts(userId)
         val alerts = alertMaps.toAlerts()
         alerts?.forEach { alert ->
            cloudRepo.downloadAlertImage(userId, alert.imageUrl, alert.id)

            // add the delay for better image loading. without it you get either partial image loaded or none
            delay(2_000)
            val reminderMaps = cloudRepo.downloadReminders(userId)
            val reminders = reminderMaps.toReminders()

            localRepo.saveAlert(alert, reminders)
         }
         onSuccess.invoke()
      } catch (e: Exception) {
         Firebase.crashlytics.recordException(e)
         onFailure.invoke()
      }
   }

   private fun getWorkerConstraints(): Constraints {
      return Constraints.Builder()
         .setRequiresCharging(false)
         .setRequiredNetworkType(NetworkType.CONNECTED)
         .build()
   }

   private fun getWorkRequest(constraints: Constraints, data: Data): OneTimeWorkRequest {
      return OneTimeWorkRequestBuilder<CloudWorker>()
         .setConstraints(constraints)
         .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(5))
         .setInputData(data)
         .build()
   }

   suspend fun scheduleBackupDownload() {
      val prefs = context.applicationContext.getSharedPreferences("ex-alert", Context.MODE_PRIVATE)
      val firstRun = prefs.getBoolean("first-run", true)
      if (firstRun) {
         notificationController.sendNotification(Notification("download", "Downloading Data..."))

         val data = Data.Builder()
            .putString(CloudWorker.USER_ID, googleClient.getUserId())
            .putInt(CloudWorker.ACTION, CloudWorker.Actions.Type.Download.ordinal)
            .build()

         val workRequest = getWorkRequest(getWorkerConstraints(), data)
         workManager.enqueueUniqueWork("backup-download", ExistingWorkPolicy.REPLACE, workRequest)

         workManager.getWorkInfosForUniqueWorkFlow("backup-download")
            .collect {
               val workInfo = it.first { it.id == workRequest.id }

               if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                  val count = workInfo.outputData.getInt(CloudWorker.DOWNLOAD_COUNT, 0)
                  notificationController.sendNotification(Notification("download", "Downloaded $count items"))

                  val editor = prefs.edit()
                  editor.putBoolean("first-run", false)
                  editor.apply()
               }

               if (workInfo.state == WorkInfo.State.FAILED) {
                  notificationController.sendNotification(Notification("download", "Download Failed"))
               }
            }
      }
   }
}

