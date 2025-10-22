package com.ns.expiration.expiration.alert.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.expiration.expiration.alert.components.textFields.TextFieldState
import com.ns.expiration.expiration.alert.services.AlertService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewmodel(
   alertService: AlertService
) : ViewModel() {

   private var _data = alertService.getActiveAlertOverviews()
   private val _state = MutableStateFlow(HomeScreenState())

   val state = combine(_data, _state) { data, state ->
      state.copy(
         alerts = data
            .filter { it.name.contains(state.searchTerm.value, ignoreCase = true) }
            .sortedBy { it.expiration }
      )
   }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = HomeScreenState()
   )

   init {
      viewModelScope.launch {
         alertService.scheduleBackupDownload()
      }
   }

   fun onAction(action: HomeScreenAction) {
      when (action) {
         is HomeScreenAction.UpdateSearchTerm -> updateSearchTerm(action.term)
      }
   }

   private fun updateSearchTerm(term: String) {
      _state.update {
         it.copy(searchTerm = TextFieldState(term, true))
      }
   }
}