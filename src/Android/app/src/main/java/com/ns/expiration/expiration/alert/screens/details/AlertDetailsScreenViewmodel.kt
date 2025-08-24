package com.ns.expiration.expiration.alert.screens.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlertDetailsScreenViewmodel(
   val id: String,
   val alertRepository: AlertRepository,
   val context: Context
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
      viewModelScope.launch(Dispatchers.IO) {
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
      viewModelScope.launch {
         _state.update { it.copy(isLoading = true) }
         try {
            alertRepository.deleteAlert(id)
            context.deleteFile("${_state.value.data.name}_${id}.webp")

            // make loading look better
            delay(1000)
            _state.update { it.copy(isLoading = true) }

            _channel.send(AlertDetailScreenEvents.AlertDeleteSuccess)
         } catch (e: Exception) {
            Log.e(AlertDetailsScreenViewmodel::class.qualifiedName, "Failed to delete alert", e)
            _channel.send(AlertDetailScreenEvents.AlertDeleteFailed)
         }
      }
   }
}