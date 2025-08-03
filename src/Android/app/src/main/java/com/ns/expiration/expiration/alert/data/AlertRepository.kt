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
            reminders = 3
         ),
         AlertOverview(
            id = UUID.randomUUID().toString(),
            name = "Alert 2",
            image = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg",
            expiration = "05/04/2026",
            reminders = 5
         ),
         AlertOverview(
            id = UUID.randomUUID().toString(),
            name = "Alert 3",
            image = "https://images.pexels.com/photos/2529468/pexels-photo-2529468.jpeg",
            expiration = "01/10/2025",
            reminders = 1
         )
      )

      return flow { emit(data) }
   }
}