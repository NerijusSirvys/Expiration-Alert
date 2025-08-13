package com.ns.expiration.expiration.alert.screens.details

sealed interface AlertDetailsScreenActions {
   data object DeleteAlert : AlertDetailsScreenActions
   data object CompleteAlert : AlertDetailsScreenActions
}