package com.ns.expiration.expiration.alert.services

import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import com.ns.expiration.expiration.alert.repositories.cloud.AlertOnCloudRepository
import com.ns.expiration.expiration.alert.repositories.local.AlertOnDiskRepository
import com.ns.expiration.expiration.alert.repositories.local.data.AlertDetails
import com.ns.expiration.expiration.alert.repositories.local.data.AlertOverview
import com.ns.expiration.expiration.alert.repositories.local.data.BackupState
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
   val cloudRepo: AlertOnCloudRepository
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

//      val alertMap = request.toAlertMap(id, imageUrl, createdOn, expirationDate)
//      val reminderMaps = request.toRemindersMapList(id, createdOn)
//      cloudRepo.uploadAlert(alertMap, reminderMaps, imageUrl)
   }

   suspend fun deleteAlert(id: String) {
      localRepo.updateAlertState(id, BackupState.PendingDelete)
   }

   fun getActiveAlertOverviews(): Flow<List<AlertOverview>> {
      return localRepo.getActiveAlertOverviews()
   }

   private fun ManageAlertScreenState.toAlertEntity(
      id: String,
      imageUri: String,
      createdOn: LocalDateTime,
      expirationDate: LocalDate,
      state: BackupState
   ): AlertEntity {
      return AlertEntity(
         id = id,
         name = this.name.value,
         quantity = this.quantity.value.toInt(),
         notes = this.notes.value,
         imageUrl = imageUri,
         expirationDate = expirationDate,
         createdOn = createdOn,
         state = state
      )
   }

   private fun ManageAlertScreenState.toReminderEntities(alertId: String, createdOn: LocalDateTime): List<ReminderEntity> {
      val reminders = mutableListOf<ReminderEntity>()
      this.reminders.forEach {
         reminders.add(
            ReminderEntity(
               id = it.id,
               alertId = alertId,
               range = it.range,
               value = it.value,
               createdOn = createdOn,
            )
         )
      }

      return reminders
   }

   private fun ManageAlertScreenState.toAlertMap(id: String, imageUri: String, createdOn: LocalDateTime, expirationDate: LocalDate): HashMap<String, Any> {
      return hashMapOf(
         "id" to id,
         "name" to this.name.value,
         "quantity" to this.quantity.value.toInt(),
         "notes" to this.notes.value,
         "imageUrl" to imageUri,
         "expirationDate" to expirationDate,
         "createdOn" to createdOn,
      )
   }

   private fun ManageAlertScreenState.toRemindersMapList(alertId: String, createdOn: LocalDateTime): List<HashMap<String, Any>> {
      return reminders.map { reminder ->
         hashMapOf(
            "id" to reminder.id,
            "alertId" to alertId,
            "range" to reminder.range,
            "value" to reminder.value,
            "createdOn" to createdOn,
         )
      }
   }
}

