package com.ns.expiration.expiration.alert.extensions

import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import com.ns.expiration.expiration.alert.repositories.local.data.BackupState
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreenState
import com.ns.expiration.expiration.alert.utilities.Constants
import java.time.LocalDate
import java.time.LocalDateTime

fun ManageAlertScreenState.toAlertMap(id: String, imageUri: String, createdOn: LocalDateTime, expirationDate: LocalDate): HashMap<String, Any> {
   return hashMapOf(
      Constants.AlertProps.ID to id,
      Constants.AlertProps.NAME to this.name.value,
      Constants.AlertProps.QUANTITY to this.quantity.value.toInt(),
      Constants.AlertProps.NOTES to this.notes.value,
      Constants.AlertProps.IMAGE_URL to imageUri,
      Constants.AlertProps.EXPIRATION_DATE to expirationDate,
      Constants.AlertProps.CREATED_ON to createdOn,
   )
}

fun ManageAlertScreenState.toRemindersMapList(alertId: String, createdOn: LocalDateTime): List<HashMap<String, Any>> {
   return reminders.map { reminder ->
      hashMapOf(
         Constants.ReminderProps.ID to reminder.id,
         Constants.ReminderProps.ALERT_ID to alertId,
         Constants.ReminderProps.RANGE to reminder.range,
         Constants.ReminderProps.VALUE to reminder.value,
         Constants.ReminderProps.CREATED_ON to createdOn,
      )
   }
}

fun ManageAlertScreenState.toReminderEntities(alertId: String, createdOn: LocalDateTime): List<ReminderEntity> {
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

fun ManageAlertScreenState.toAlertEntity(
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