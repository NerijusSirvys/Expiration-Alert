package com.ns.expiration.expiration.alert.persistance.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "reminders")
data class ReminderEntity(
   @PrimaryKey(autoGenerate = false)
   val id: String = UUID.randomUUID().toString(),
   val alertId: String,
   val range: ReminderRange,
   val value: Int,
   val createdOn: LocalDateTime,
   val modifiedOn: LocalDateTime? = null
)