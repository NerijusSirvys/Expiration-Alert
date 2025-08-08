package com.ns.expiration.expiration.alert.screens.details

import com.ns.expiration.expiration.alert.data.AlertDetails

data class AlertDetailsScreenState(
   val isLoading: Boolean = true,
   val data: AlertDetails = AlertDetails()
)