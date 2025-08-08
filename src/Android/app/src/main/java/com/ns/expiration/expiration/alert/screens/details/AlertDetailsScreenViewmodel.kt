package com.ns.expiration.expiration.alert.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.expiration.expiration.alert.data.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AlertDetailsScreenViewmodel(val id: String, alertRepository: AlertRepository) : ViewModel() {

   private val _details = alertRepository.getAlertById(id)
   private val _state = MutableStateFlow(AlertDetailsScreenState())

   val state = combine(_details, _state) { details, state ->
      state.copy(
         isLoading = false,
         data = details
      )
   }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = AlertDetailsScreenState()
   )
}