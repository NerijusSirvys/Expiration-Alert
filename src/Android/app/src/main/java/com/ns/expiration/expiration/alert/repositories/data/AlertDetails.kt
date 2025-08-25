package com.ns.expiration.expiration.alert.repositories.data

data class AlertDetails(
   val id: String = "",
   val name: String = "",
   val quantity: Int = 0,
   val notes: String = "",
   val expirationDate: String = "",
   val reminders: List<Reminder> = emptyList(),
   val imageUrl: String = "",
)