package com.ns.expiration.expiration.alert.services

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.ns.expiration.expiration.alert.GoogleSignInClient
import com.ns.expiration.expiration.alert.extensions.toAlertEntity
import com.ns.expiration.expiration.alert.extensions.toAlerts
import com.ns.expiration.expiration.alert.extensions.toReminderEntities
import com.ns.expiration.expiration.alert.extensions.toReminders
import com.ns.expiration.expiration.alert.repositories.cloud.AlertOnCloudRepository
import com.ns.expiration.expiration.alert.repositories.local.AlertOnDiskRepository
import com.ns.expiration.expiration.alert.repositories.local.data.AlertDetails
import com.ns.expiration.expiration.alert.repositories.local.data.AlertOverview
import com.ns.expiration.expiration.alert.repositories.local.data.BackupState
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreenState
import com.ns.expiration.expiration.alert.utilities.DateTimeHelpers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AlertService(
   val localRepo: AlertOnDiskRepository,
   val cloudRepo: AlertOnCloudRepository,
   val googleClient: GoogleSignInClient
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

      val alert = request.toAlertEntity(id, imageUrl, createdOn, expirationDate, BackupState.PendingUpload)
      val reminders = request.toReminderEntities(id, createdOn)
      localRepo.saveAlert(alert, reminders)
   }

   suspend fun deleteAlert(id: String) {
      localRepo.updateAlertState(id, BackupState.PendingDelete)
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
            if (reminders == null) return

            localRepo.saveAlert(alert, reminders)
         }
         onSuccess.invoke()
      } catch (e: Exception) {
         Firebase.crashlytics.recordException(e)
         onFailure.invoke()
      }
   }
}

