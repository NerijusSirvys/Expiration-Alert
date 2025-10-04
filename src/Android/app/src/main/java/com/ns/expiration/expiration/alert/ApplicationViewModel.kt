package com.ns.expiration.expiration.alert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.expiration.expiration.alert.navigation.Destinations
import com.ns.expiration.expiration.alert.services.AlertService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ApplicationState(
   val syncing: Boolean = false
)

class ApplicationViewModel(
   val googleClient: GoogleSignInClient,
   private val service: AlertService
) : ViewModel() {

   private val _state = MutableStateFlow(ApplicationState())
   val state = _state.stateIn(
      started = SharingStarted.WhileSubscribed(5_000),
      scope = viewModelScope,
      initialValue = ApplicationState()
   )

   val destination = if (googleClient.signedIn()) Destinations.Home
   else Destinations.Authentication

   fun onAction(actions: ApplicationActions) {
      when (actions) {
         ApplicationActions.Sync -> syncData()
      }
   }

   private fun syncData() {
      viewModelScope.launch {
         _state.update { it.copy(syncing = true) }
         service.downloadBackups()
         delay(15_000)
         _state.update { it.copy(syncing = false) }
      }
   }

   fun setupTokenRefreshListener(onRefreshFailed: () -> Unit) {
      googleClient.setupTokenRefreshListener(onRefreshFailed)
   }
}

sealed interface ApplicationActions {
   data object Sync : ApplicationActions
}