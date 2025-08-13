package com.ns.expiration.expiration.alert.persistance.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AlertWithReminders(
   @Embedded
   val alert: AlertEntity,

   @Relation(
      parentColumn = "id",
      entityColumn = "alertId"
   )
   val reminders: List<ReminderEntity>
)
