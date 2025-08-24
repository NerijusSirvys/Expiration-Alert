package com.ns.expiration.expiration.alert.screens.create.data

import com.ns.expiration.expiration.alert.repositories.data.ReminderRange

data class Reminder(
   val id: String,
   val range: ReminderRange,
   val value: Int
) {
   override fun toString(): String {
      val singular = range.name.substring(0, range.name.length - 1)
      return when (value) {
         1 -> "$value $singular until expiration"
         else -> "$value ${range.name} until expiration"
      }
   }
}
