package com.ns.expiration.expiration.alert.extensions

import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import com.ns.expiration.expiration.alert.repositories.local.data.BackupState
import com.ns.expiration.expiration.alert.repositories.local.data.ReminderRange
import com.ns.expiration.expiration.alert.utilities.Constants
import java.time.LocalDate
import java.time.LocalDateTime

fun List<Map<String?, Any?>>?.toAlerts(): List<AlertEntity>? {
   return this?.map { map ->
      (AlertEntity(
         id = map[Constants.AlertProps.ID].toString(),
         name = map[Constants.AlertProps.NAME].toString(),
         quantity = map[Constants.AlertProps.QUANTITY].toString().toInt(),
         notes = map[Constants.AlertProps.NOTES].toString(),
         imageUrl = map[Constants.AlertProps.IMAGE_URL].toString(),
         expirationDates = (map[Constants.AlertProps.EXPIRATION_DATES] as List<*>).map { LocalDate.parse(it.toString()) },
         createdOn = LocalDateTime.parse(map[Constants.AlertProps.CREATED_ON].toString()),
         state = BackupState.Uploaded,
      ))
   }
}

fun List<Map<String?, Any?>>.toReminders(): List<ReminderEntity>? {
   return this.map { map ->
      ReminderEntity(
         id = map[Constants.ReminderProps.ID].toString(),
         alertId = map[Constants.ReminderProps.ALERT_ID].toString(),
         range = ReminderRange.valueOf(map[Constants.ReminderProps.RANGE].toString()),
         value = map[Constants.ReminderProps.VALUE].toString().toInt(),
         createdOn = LocalDateTime.parse(map[Constants.ReminderProps.CREATED_ON].toString()),
      )
   }
}