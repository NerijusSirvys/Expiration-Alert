package com.ns.expiration.expiration.alert.data

data class AlertDetails(
   val id: String = "",
   val name: String = "",
   val quantity: Int = 0,
   val notes: String = "",
   val expirationDate: String = "",
   val reminders: List<String> = emptyList(),
   val imageUrl: String = ""
)