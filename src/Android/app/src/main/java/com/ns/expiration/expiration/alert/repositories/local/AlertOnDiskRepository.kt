package com.ns.expiration.expiration.alert.repositories.local

import com.ns.expiration.expiration.alert.persistance.dao.AlertDao
import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.AlertWithReminders
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import com.ns.expiration.expiration.alert.repositories.local.data.AlertDetails
import com.ns.expiration.expiration.alert.repositories.local.data.AlertOverview
import com.ns.expiration.expiration.alert.repositories.local.data.Reminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class AlertOnDiskRepository(
   val alertDao: AlertDao,
) {
   suspend fun getActiveAlerts(): List<AlertWithReminders> {
      return alertDao.getAllAlertsWithReminders().first()
   }

   fun getActiveAlertOverviews(): Flow<List<AlertOverview>> {
      return alertDao.getAllAlertsWithReminders().mapLatest { dataList ->
         dataList.map {
            AlertOverview(
               id = it.alert.id,
               name = it.alert.name,
               quantity = it.alert.quantity,
               image = it.alert.imageUrl,
               expiration = it.alert.expirationDate.toString(),
               reminders = it.reminders.size
            )
         }
      }
   }

   suspend fun getAlertById(id: String): Flow<AlertDetails> {
      val data = withContext(Dispatchers.IO) {
         alertDao.getAlertWithReminders(id).let { alert ->
            AlertDetails(
               id = alert.alert.id,
               name = alert.alert.name,
               quantity = alert.alert.quantity,
               notes = alert.alert.notes,
               expirationDate = alert.alert.expirationDate.toString(),
               reminders = alert.reminders.map {
                  Reminder(
                     id = it.id,
                     range = it.range,
                     value = it.value
                  )
               },
               imageUrl = alert.alert.imageUrl,
            )
         }
      }

      return flow { emit(data) }
   }

   suspend fun deleteAlert(id: String): AlertWithReminders = withContext(Dispatchers.IO) {
      if (id.isEmpty())
         throw IllegalArgumentException("Id cannot be empty")

      return@withContext alertDao.deleteAlertWithReminders(id)
   }

   suspend fun saveAlert(alert: AlertEntity, reminders: List<ReminderEntity>) = withContext(Dispatchers.IO) {
      alertDao.insertAlertWithReminders(AlertWithReminders(alert, reminders))
   }
}