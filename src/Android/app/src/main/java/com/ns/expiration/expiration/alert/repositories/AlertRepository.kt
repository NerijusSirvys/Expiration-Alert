package com.ns.expiration.expiration.alert.repositories

import androidx.core.net.toUri
import coil3.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ns.expiration.expiration.alert.persistance.dao.AlertDao
import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.AlertWithReminders
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import com.ns.expiration.expiration.alert.repositories.data.AlertDetails
import com.ns.expiration.expiration.alert.repositories.data.AlertOverview
import com.ns.expiration.expiration.alert.repositories.data.Reminder
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreenState
import com.ns.expiration.expiration.alert.utilities.DateTimeHelpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class AlertRepository(
   val alertDao: AlertDao,
   val firestore: FirebaseFirestore,
   val storage: FirebaseStorage
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

   suspend fun deleteAlert(id: String) = withContext(Dispatchers.IO) {
      if (id.isEmpty())
         throw IllegalArgumentException("Id cannot be empty")

      alertDao.deleteAlertWithReminders(id)
   }

   suspend fun saveAlert(id: String, imageUrl: String, request: ManageAlertScreenState) = withContext(Dispatchers.IO) {
      val formatter = DateTimeFormatter.ofPattern(DateTimeHelpers.FORMAT)
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


         Firebase.auth.currentUser?.let { user ->
            val alert_rec = hashMapOf(
               "id" to id,
               "name" to request.name.value,
               "quantity" to request.quantity.value.toInt(),
               "notes" to request.notes.value,
               "imageUrl" to imageUrl,
               "expirationDate" to expirationDate,
               "createdOn" to createdOn,
            )

            firestore.collection("users").document(user.uid)
               .collection("alerts").document(alert.id)
               .set(alert_rec)

            reminders.forEach { reminder ->
               val reminder_rec = hashMapOf(
                  "id" to it.id,
                  "alertId" to alert.id,
                  "range" to it.range,
                  "value" to it.value,
                  "createdOn" to createdOn,
               )

               firestore.collection("users").document(user.uid)
                  .collection("reminders").document(reminder.id)
                  .set(reminder_rec)
            }

            val imageUri = Uri(imageUrl)
            val imageName = imageUri.scheme?.split(imageUri.separator)?.last()

            val img = storage.reference.child("${user.uid}/images/${alert.id}/$imageName")
            val file = File(alert.imageUrl)

            img.putFile(file.toUri())
         }
      }

      alertDao.insertAlertWithReminders(AlertWithReminders(alert, reminders))
   }
}