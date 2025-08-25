package com.ns.expiration.expiration.alert.screens.manage

import coil3.Bitmap
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange

sealed interface ManageAlertScreenActions {
   data class UpdateName(val value: String) : ManageAlertScreenActions
   data class UpdateNotes(val value: String) : ManageAlertScreenActions
   data class UpdateQuantity(val value: String) : ManageAlertScreenActions
   data class SetExpirationDate(val value: String) : ManageAlertScreenActions
   data object Save : ManageAlertScreenActions
   data class ManageReminder(val value: Int, val range: ReminderRange) : ManageAlertScreenActions
   data class DeleteReminder(val id: String) : ManageAlertScreenActions
   data class TakePicture(val bitmap: Bitmap) : ManageAlertScreenActions
   data object ResetPicture : ManageAlertScreenActions
}