package com.ns.expiration.expiration.alert.screens.create

import androidx.lifecycle.ViewModel
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateAlertScreenViewmodel(
   val repository: AlertRepository
) : ViewModel() {
   private val _state = MutableStateFlow(CreateNewAlertScreenState())
   val state = _state.asStateFlow()

   fun onAction(action: CreateAlertScreenActions) {
      when (action) {
         is CreateAlertScreenActions.SetExpirationDate -> setExpirationDate(action.value)
         is CreateAlertScreenActions.UpdateName -> updateName(action.value)
         is CreateAlertScreenActions.UpdateNotes -> updateNotes(action.value)
         CreateAlertScreenActions.Save -> save()
      }
   }

   private fun save() {
      TODO("Not yet implemented")
   }

   private fun setExpirationDate(value: Long) {
      _state.update {
         it.copy(expirationDate = value)
      }
   }

   private fun updateNotes(value: String) {
      _state.update {
         it.copy(notes = value)
      }
   }

   private fun updateName(value: String) {
      _state.update {
         it.copy(name = value)
      }
   }
}