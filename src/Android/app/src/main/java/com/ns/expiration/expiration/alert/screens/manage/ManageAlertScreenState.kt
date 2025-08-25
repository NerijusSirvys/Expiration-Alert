package com.ns.expiration.expiration.alert.screens.manage

import coil3.Bitmap
import com.ns.expiration.expiration.alert.components.textFields.TextFieldState
import com.ns.expiration.expiration.alert.repositories.data.Reminder

data class ManageAlertScreenState(
   val name: TextFieldState = TextFieldState("", true),
   val notes: TextFieldState = TextFieldState("", true),
   val quantity: TextFieldState = TextFieldState("", true),
   val expirationDate: TextFieldState = TextFieldState("", true),
   val reminders: List<Reminder> = emptyList(),
   val image: Bitmap? = null,
   val isLoading: Boolean = false
)
