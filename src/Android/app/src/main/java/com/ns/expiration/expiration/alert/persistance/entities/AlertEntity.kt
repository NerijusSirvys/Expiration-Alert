package com.ns.expiration.expiration.alert.persistance.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ns.expiration.expiration.alert.data.AlertState
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "alerts")
data class AlertEntity(
   @PrimaryKey(autoGenerate = false)
   val id: String = UUID.randomUUID().toString(),
   val name: String,
   val quantity: Int,
   val notes: String,
   val imageUrl: String,
   val state: AlertState,
   val expirationDate: LocalDate,
   val createdOn: LocalDateTime,
   val modifiedOn: LocalDateTime? = null
)

