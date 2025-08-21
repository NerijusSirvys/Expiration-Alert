package com.ns.expiration.expiration.alert.screens.create

sealed interface CreateAlertScreenActions {
   data class UpdateName(val value: String) : CreateAlertScreenActions
   data class UpdateNotes(val value: String) : CreateAlertScreenActions
   data class SetExpirationDate(val value: Long) : CreateAlertScreenActions
   data object Save : CreateAlertScreenActions
}