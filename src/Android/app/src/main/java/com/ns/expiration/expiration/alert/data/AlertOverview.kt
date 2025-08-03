package com.ns.expiration.expiration.alert.data

data class AlertOverview(
   val id: String,
   val name: String,
   val image: String,
   val expiration: String,
   val reminders: Int
)
