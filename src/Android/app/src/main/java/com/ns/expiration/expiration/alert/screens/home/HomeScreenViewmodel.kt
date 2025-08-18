package com.ns.expiration.expiration.alert.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeScreenViewmodel(
   alertRepository: AlertRepository
) : ViewModel() {

   private var _data = alertRepository.getActiveAlertOverviews()
   private val _state = MutableStateFlow(HomeScreenState())

   val state = combine(_data, _state) { data, state ->
      state.copy(
         alerts = data.filter { it.name.contains(state.searchTerm) }
      )
   }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = HomeScreenState()
   )

   fun onAction(action: HomeScreenAction) {
      when (action) {
         is HomeScreenAction.UpdateSearchTerm -> updateSearchTerm(action.term)
      }
   }

   private fun updateSearchTerm(term: String) {
      _state.update {
         it.copy(searchTerm = term)
      }
   }
}

