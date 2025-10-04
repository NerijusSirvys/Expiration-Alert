package com.ns.expiration.expiration.alert.services

import com.ns.expiration.expiration.alert.GoogleSignInClient
import com.ns.expiration.expiration.alert.extensions.toAlertEntity
import com.ns.expiration.expiration.alert.extensions.toReminderEntities
import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import com.ns.expiration.expiration.alert.repositories.cloud.AlertOnCloudRepository
import com.ns.expiration.expiration.alert.repositories.local.AlertOnDiskRepository
import com.ns.expiration.expiration.alert.repositories.local.data.AlertDetails
import com.ns.expiration.expiration.alert.repositories.local.data.AlertOverview
import com.ns.expiration.expiration.alert.repositories.local.data.BackupState
import com.ns.expiration.expiration.alert.repositories.local.data.ReminderRange
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreenState
import com.ns.expiration.expiration.alert.utilities.DateTimeHelpers
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
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

   suspend fun downloadBackups() {
      val userId = googleClient.getUserId()
      if (userId.isEmpty()) return

      val alertMaps = cloudRepo.downloadAlerts(userId)
      val alerts = alertMaps.toAlerts()
      alerts?.forEach { alert ->
         cloudRepo.downloadAlertImage(userId, alert.imageUrl, alert.id)

         val reminderMaps = cloudRepo.downloadReminders(userId)
         val reminders = reminderMaps.toReminders()
         if (reminders == null) return
         localRepo.saveAlert(alert, reminders)
      }
   }


   private fun List<Map<String?, Any?>>?.toAlerts(): List<AlertEntity>? {
      return this?.map { map ->
         (AlertEntity(
            id = map["id"].toString(),
            name = map["name"].toString(),
            quantity = map["quantity"].toString().toInt(),
            notes = map["notes"].toString(),
            imageUrl = map["imageUrl"].toString(),
            expirationDate = LocalDate.parse(map["expirationDate"].toString()),
            createdOn = LocalDateTime.parse(map["createdOn"].toString()),
            state = BackupState.Uploaded,
         ))
      }
   }

   private fun List<Map<String?, Any?>>.toReminders(): List<ReminderEntity>? {
      return this.map { map ->
         ReminderEntity(
            id = map["id"].toString(),
            alertId = map["alertId"].toString(),
            range = ReminderRange.valueOf(map["range"].toString()),
            value = map["value"].toString().toInt(),
            createdOn = LocalDateTime.parse(map["createdOn"].toString()),
         )
      }
   }
}

