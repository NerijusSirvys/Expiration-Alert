package com.ns.expiration.expiration.alert.navigation

import kotlinx.serialization.Serializable

sealed interface Destinations {

   @Serializable
   data object Home : Destinations

   @Serializable
   data object NewAlert : Destinations
}