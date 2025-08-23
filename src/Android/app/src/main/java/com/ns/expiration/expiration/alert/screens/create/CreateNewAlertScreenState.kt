package com.ns.expiration.expiration.alert.screens.create

import coil3.Bitmap
import com.ns.expiration.expiration.alert.screens.create.data.Reminder

data class CreateNewAlertScreenState(
   val name: String = "",
   val notes: String = "",
   val expirationDate: Long? = null,
   val reminders: List<Reminder> = emptyList(),
   val image: Bitmap? = null
)
