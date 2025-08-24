package com.ns.expiration.expiration.alert.screens.create

sealed interface CreateAlertScreenEvents {
   data object AlertSavedSuccess : CreateAlertScreenEvents
   data object AlertSaveFailed : CreateAlertScreenEvents
   data object ReminderDuplicateFound : CreateAlertScreenEvents
   data object MissingInformation : CreateAlertScreenEvents
}