package com.ns.expiration.expiration.alert.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ns.expiration.expiration.alert.persistance.converters.LocalDateConverter
import com.ns.expiration.expiration.alert.persistance.converters.LocalDateTimeConverter
import com.ns.expiration.expiration.alert.persistance.dao.AlertDao
import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.AlertWithReminders
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime


@Database(
   entities = [AlertEntity::class, ReminderEntity::class],
   version = 1
)
@TypeConverters(
   LocalDateConverter::class,
   LocalDateTimeConverter::class
)
abstract class AlertDatabase() : RoomDatabase() {
   abstract fun alertDao(): AlertDao

   companion object {
      fun prepopulateDatabase(database: AlertDatabase) {
         CoroutineScope(Dispatchers.IO).launch {
            val dao = database.alertDao()
            val alerts = getPredefinedAlerts()
            alerts.forEach { alertWithReminders ->
               dao.insertAlert(alertWithReminders.alert)
               alertWithReminders.reminders.forEach { reminder ->
                  dao.insertReminder(reminder.copy(alertId = alertWithReminders.alert.id))
               }
            }
         }
      }

      private fun getPredefinedAlerts(): List<AlertWithReminders> {
         return listOf(
            AlertWithReminders(
               alert = AlertEntity(
                  name = "Alert 1",
                  quantity = 3,
                  notes = "Some Random Stuff",
                  imageUrl = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg",
                  expirationDate = LocalDate.now().plusDays(13),
                  createdOn = LocalDateTime.now(),
               ),
               reminders = listOf(
                  ReminderEntity(
                     alertId = "",
                     range = ReminderRange.Days,
                     value = 6,
                     createdOn = LocalDateTime.now()
                  )
               )
            ),
            AlertWithReminders(
               alert = AlertEntity(
                  name = "Alert 2",
                  quantity = 1,
                  notes = "Some Random Stuff about second alert",
                  imageUrl = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg",
                  expirationDate = LocalDate.now().plusDays(4),
                  createdOn = LocalDateTime.now(),
               ),
               reminders = listOf(
                  ReminderEntity(
                     alertId = "",
                     range = ReminderRange.Days,
                     value = 2,
                     createdOn = LocalDateTime.now()
                  ),
                  ReminderEntity(
                     alertId = "",
                     range = ReminderRange.Days,
                     value = 1,
                     createdOn = LocalDateTime.now()
                  )
               )
            ),
            AlertWithReminders(
               alert = AlertEntity(
                  name = "Alert 3",
                  quantity = 25,
                  notes = "Some Random Stuff again",
                  imageUrl = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg",
                  expirationDate = LocalDate.now().plusDays(30),
                  createdOn = LocalDateTime.now(),
               ),
               reminders = listOf(
                  ReminderEntity(
                     alertId = "",
                     range = ReminderRange.Days,
                     value = 10,
                     createdOn = LocalDateTime.now()
                  ),
                  ReminderEntity(
                     alertId = "",
                     range = ReminderRange.Weeks,
                     value = 2,
                     createdOn = LocalDateTime.now()
                  ),
                  ReminderEntity(
                     alertId = "",
                     range = ReminderRange.Days,
                     value = 1,
                     createdOn = LocalDateTime.now()
                  )
               )
            ),
         )
      }
   }
}