package com.ns.expiration.expiration.alert.repositories.data

data class AlertOverview(
   val id: String,
   val name: String,
   val quantity: Int,
   val image: String,
   val expiration: String,
   val reminders: Int
)
