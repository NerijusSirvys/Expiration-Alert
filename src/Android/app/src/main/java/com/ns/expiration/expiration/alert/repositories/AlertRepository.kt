package com.ns.expiration.expiration.alert.repositories

import com.ns.expiration.expiration.alert.persistance.dao.AlertDao
import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.AlertWithReminders
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import com.ns.expiration.expiration.alert.repositories.data.AlertDetails
import com.ns.expiration.expiration.alert.repositories.data.AlertOverview
import com.ns.expiration.expiration.alert.screens.create.CreateNewAlertScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class AlertRepository(
   val alertDao: AlertDao
) {
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
      val data = alertDao.getAlertWithReminders(id).let { alert ->
         AlertDetails(
            id = alert.alert.id,
            name = alert.alert.name,
            quantity = alert.alert.quantity,
            notes = alert.alert.notes,
            expirationDate = alert.alert.expirationDate.toString(),
            reminders = alert.reminders.map {
               "${it.value} ${it.range} until expiration"
            },
            imageUrl = alert.alert.imageUrl,
         )
      }

      return flow { emit(data) }
   }

   suspend fun deleteAlert(id: String) {
      if (id.isEmpty())
         throw IllegalArgumentException("Id cannot be empty")

      alertDao.deleteAlertWithReminders(id)
   }

   suspend fun saveAlert(id: String, imageUrl: String, request: CreateNewAlertScreenState): String {
      val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
      val expirationDate = LocalDate.parse(request.expirationDate.value, formatter)

      val createdOn = Instant.ofEpochMilli(System.currentTimeMillis())
         .atZone(ZoneId.systemDefault())
         .toLocalDateTime()

      val alert = AlertEntity(
         id = id,
         name = request.name.value,
         quantity = request.quantity.value.toInt(),
         notes = request.notes.value,
         imageUrl = imageUrl,
         expirationDate = expirationDate,
         createdOn = createdOn,
      )

      val reminders = mutableListOf<ReminderEntity>()
      request.reminders.forEach {
         reminders.add(
            ReminderEntity(
               id = it.id,
               alertId = alert.id,
               range = it.range,
               value = it.value,
               createdOn = createdOn,
            )
         )
      }

      alertDao.insertAlertWithReminders(AlertWithReminders(alert, reminders))

      return alert.id;
   }
}