package com.ns.expiration.expiration.alert.screens.create.data

import com.ns.expiration.expiration.alert.repositories.data.ReminderRange

data class Reminder(
   val range: ReminderRange,
   val value: Int
)
