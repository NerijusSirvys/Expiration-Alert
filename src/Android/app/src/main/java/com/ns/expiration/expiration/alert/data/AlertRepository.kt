package com.ns.expiration.expiration.alert.data

import com.ns.expiration.expiration.alert.persistance.dao.AlertDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
class AlertRepository(
   val alertDao: AlertDao
) {
   fun getAlertOverviews(): Flow<List<AlertOverview>> {
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
            state = alert.alert.state
         )
      }

      return flow { emit(data) }
   }

   suspend fun completeAlert(id: String) {
      if (id.isEmpty())
         throw IllegalArgumentException("Id cannot be empty")

      val alert = alertDao.getAlert(id)
      alertDao.updateAlert(alert.copy(state = AlertState.Complete))
   }

   suspend fun deleteAlert(id: String) {
      if (id.isEmpty())
         throw IllegalArgumentException("Id cannot be empty")

      throw IllegalArgumentException("Id cannot be empty")
      alertDao.deleteAlertWithReminders(id)
   }
}