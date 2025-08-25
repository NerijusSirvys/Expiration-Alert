package com.ns.expiration.expiration.alert.navigation

import kotlinx.serialization.Serializable

sealed interface Destinations {

   @Serializable
   data object Home : Destinations

   @Serializable
   data class AlertDetails(val id: String) : Destinations

   @Serializable
   data class ManageAlert(val id: String?) : Destinations
}