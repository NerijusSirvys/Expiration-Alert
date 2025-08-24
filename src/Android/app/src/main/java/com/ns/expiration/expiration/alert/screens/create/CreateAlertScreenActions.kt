package com.ns.expiration.expiration.alert.screens.create

import coil3.Bitmap
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange

sealed interface CreateAlertScreenActions {
   data class UpdateName(val value: String) : CreateAlertScreenActions
   data class UpdateNotes(val value: String) : CreateAlertScreenActions
   data class UpdateQuantity(val value: String) : CreateAlertScreenActions
   data class SetExpirationDate(val value: String) : CreateAlertScreenActions
   data object Save : CreateAlertScreenActions
   data class CreateReminder(val value: Int, val range: ReminderRange) : CreateAlertScreenActions
   data class DeleteReminder(val id: String) : CreateAlertScreenActions
   data class TakePicture(val bitmap: Bitmap) : CreateAlertScreenActions
   data object ResetPicture : CreateAlertScreenActions
}