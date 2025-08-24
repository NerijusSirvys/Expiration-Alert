package com.ns.expiration.expiration.alert.screens.details

sealed interface AlertDetailScreenEvents {
   data object AlertDeleteSuccess : AlertDetailScreenEvents
   data object AlertDeleteFailed : AlertDetailScreenEvents
}

