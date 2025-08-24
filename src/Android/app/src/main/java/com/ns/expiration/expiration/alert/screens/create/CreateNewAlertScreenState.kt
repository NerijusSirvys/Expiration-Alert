package com.ns.expiration.expiration.alert.screens.create

import coil3.Bitmap
import com.ns.expiration.expiration.alert.components.textFields.TextFieldState
import com.ns.expiration.expiration.alert.screens.create.data.Reminder

data class CreateNewAlertScreenState(
   val name: TextFieldState = TextFieldState("", true),
   val notes: TextFieldState = TextFieldState("", true),
   val quantity: TextFieldState = TextFieldState("", true),
   val expirationDate: TextFieldState = TextFieldState("", true),
   val reminders: List<Reminder> = emptyList(),
   val image: Bitmap? = null,
   val isLoading: Boolean = false
)
