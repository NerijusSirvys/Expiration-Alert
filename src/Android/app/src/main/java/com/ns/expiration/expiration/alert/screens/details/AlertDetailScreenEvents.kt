package com.ns.expiration.expiration.alert.screens.details

sealed interface AlertDetailScreenEvents {
   data class AlertDelete(val result: AlertActionResult) : AlertDetailScreenEvents
   data class AlertComplete(val result: AlertActionResult) : AlertDetailScreenEvents
}

enum class AlertActionResult {
   Success,
   Failed
}
