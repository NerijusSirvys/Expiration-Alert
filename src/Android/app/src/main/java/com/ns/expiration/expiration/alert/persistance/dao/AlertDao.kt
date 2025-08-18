package com.ns.expiration.expiration.alert.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.AlertWithReminders
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

   @Transaction
   @Query("SELECT * FROM alerts")
   fun getAllAlertsWithReminders(): Flow<List<AlertWithReminders>>

   @Transaction
   @Query("SELECT * FROM alerts WHERE id = :id")
   suspend fun getAlertWithReminders(id: String): AlertWithReminders

   @Insert(onConflict = REPLACE)
   suspend fun insertReminder(data: ReminderEntity)

   @Insert(onConflict = REPLACE)
   suspend fun insertAlert(data: AlertEntity)

   @Delete()
   suspend fun deleteAlert(alert: AlertEntity)

   @Delete()
   suspend fun deleteReminder(reminder: ReminderEntity)

   @Query("SELECT * FROM alerts WHERE id = :id")
   suspend fun getAlert(id: String): AlertEntity

   @Update()
   suspend fun updateAlert(alertEntity: AlertEntity)

   @Transaction
   suspend fun deleteAlertWithReminders(alertId: String) {

      val alert = getAlertWithReminders(alertId)

      alert.reminders.forEach {
         deleteReminder(it)
      }

      deleteAlert(alert.alert)
   }
}
