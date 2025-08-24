package com.ns.expiration.expiration.alert.screens.home

import com.ns.expiration.expiration.alert.components.textFields.TextFieldState
import com.ns.expiration.expiration.alert.repositories.data.AlertOverview

data class HomeScreenState(
   val isLoading: Boolean = false,
   val alerts: List<AlertOverview> = emptyList(),
   val searchTerm: TextFieldState = TextFieldState("", true)
)
