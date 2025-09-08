package com.ns.expiration.expiration.alert.notifications

import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity

data class Notification(
   val id: String,
   val text: String
) {
   companion object {
      fun fromAlert(alert: AlertEntity): Notification {
         return Notification(
            id = alert.id,
            text = "${alert.name} expires on ${alert.expirationDate}"
         )
      }
   }
}
