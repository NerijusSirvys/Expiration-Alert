package com.ns.expiration.expiration.alert.extensions

import com.ns.expiration.expiration.alert.persistance.entities.AlertWithReminders
import com.ns.expiration.expiration.alert.utilities.Constants

fun AlertWithReminders.toReminderMap(): List<HashMap<String, Any>> {
   return this.reminders.map { reminder ->
      hashMapOf(
         Constants.ReminderProps.ID to reminder.id,
         Constants.ReminderProps.ALERT_ID to this.alert.id,
         Constants.ReminderProps.RANGE to reminder.range,
         Constants.ReminderProps.VALUE to reminder.value,
         Constants.ReminderProps.CREATED_ON to reminder.createdOn.toString(),
      )
   }
}

fun AlertWithReminders.toAlertMap(): HashMap<String, Any> {
   return hashMapOf(
      Constants.AlertProps.ID to this.alert.id,
      Constants.AlertProps.NAME to this.alert.name,
      Constants.AlertProps.QUANTITY to this.alert.quantity,
      Constants.AlertProps.NOTES to this.alert.notes,
      Constants.AlertProps.IMAGE_URL to this.alert.imageUrl,
      Constants.AlertProps.EXPIRATION_DATES to this.alert.expirationDates.toString(),
      Constants.AlertProps.CREATED_ON to this.alert.createdOn.toString(),
   )
}

fun AlertWithReminders.reminderIds(): List<String> {
   return this.reminders.map { it.id }
}

fun AlertWithReminders.imageName(): String {
   return this.alert.imageUrl.split("/").last()
}

fun AlertWithReminders.alertId(): String {
   return this.alert.id
}

fun AlertWithReminders.imageUrl(): String {
   return this.alert.imageUrl
}