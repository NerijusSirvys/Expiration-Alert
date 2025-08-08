package com.ns.expiration.expiration.alert.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class AlertRepository {
   fun getAllAlerts(): Flow<List<AlertOverview>> {
      val data = listOf(
         AlertOverview(
            id = UUID.randomUUID().toString(),
            name = "Alert 1",
            image = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg",
            expiration = "11/10/2025",
            reminders = 3,
            quantity = 5
         ),
         AlertOverview(
            id = UUID.randomUUID().toString(),
            name = "Alert 2",
            image = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg",
            expiration = "05/04/2026",
            reminders = 5,
            quantity = 1
         ),
         AlertOverview(
            id = UUID.randomUUID().toString(),
            name = "Alert 3",
            image = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg",
            expiration = "01/10/2025",
            reminders = 1,
            quantity = 15
         )
      )

      return flow { emit(data) }
   }

   fun getAlertById(id: String): Flow<AlertDetails> {
      val details = AlertDetails(
         id = id,
         name = "My Alert",
         quantity = 15,
         notes = "Some information about the alert that should be very important to know",
         expirationDate = "01/10/2025",
         reminders = listOf("2 days until expiration", "14 days until expiration"),
         imageUrl = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg"
      )

      return flow { emit(details) }
   }
}