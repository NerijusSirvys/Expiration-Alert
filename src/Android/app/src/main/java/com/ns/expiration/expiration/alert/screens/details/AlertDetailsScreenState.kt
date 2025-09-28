package com.ns.expiration.expiration.alert.screens.details

import com.ns.expiration.expiration.alert.repositories.local.data.AlertDetails

data class AlertDetailsScreenState(
   val isLoading: Boolean = true,
   val data: AlertDetails = AlertDetails()
)