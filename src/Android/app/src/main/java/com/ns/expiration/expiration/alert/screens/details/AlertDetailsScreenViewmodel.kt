package com.ns.expiration.expiration.alert.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlertDetailsScreenViewmodel(
   val id: String,
   val alertRepository: AlertRepository
) : ViewModel() {

   private val _state = MutableStateFlow(AlertDetailsScreenState())
   private val _channel = Channel<AlertDetailScreenEvents>()

   val eventChannel = _channel.receiveAsFlow()
   val state = _state.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = AlertDetailsScreenState()
   )

   init {
      viewModelScope.launch {
         alertRepository.getAlertById(id).collect { details ->
            _state.update {
               it.copy(isLoading = false, data = details)
            }
         }
      }
   }

   fun onAction(action: AlertDetailsScreenActions) {
      when (action) {
         AlertDetailsScreenActions.DeleteAlert -> deleteAlert()
      }
   }

   private fun deleteAlert() {
      viewModelScope.launch(Dispatchers.IO) {
         try {
            alertRepository.deleteAlert(id)
            _channel.send(AlertDetailScreenEvents.AlertDelete(AlertActionResult.Success))
         } catch (e: Exception) {
            Log.e(AlertDetailsScreenViewmodel::class.qualifiedName, "Failed to delete alert", e)
            _channel.send(AlertDetailScreenEvents.AlertDelete(AlertActionResult.Failed))
         }
      }
   }
}