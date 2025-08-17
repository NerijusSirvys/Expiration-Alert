package com.ns.expiration.expiration.alert.persistance.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
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
   fun getAlertDetails(id: String): Flow<AlertWithReminders>

   @Insert(onConflict = REPLACE)
   suspend fun insertReminder(data: ReminderEntity)

   @Insert(onConflict = REPLACE)
   suspend fun insertAlert(data: AlertEntity)
}
