package com.ns.expiration.expiration.alert.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import com.ns.expiration.expiration.alert.screens.create.data.Reminder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import java.util.UUID

class CreateAlertScreenViewmodel(
   val repository: AlertRepository
) : ViewModel() {

   private val _channel = Channel<String>()
   val messageChannel = _channel.receiveAsFlow()

   private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
   private val _state = MutableStateFlow(CreateNewAlertScreenState())
   val state = combine(_state, _reminders) { state, reminders ->
      state.copy(
         reminders = reminders
      )
   }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = CreateNewAlertScreenState()
   )

   fun onAction(action: CreateAlertScreenActions) {
      when (action) {
         is CreateAlertScreenActions.SetExpirationDate -> setExpirationDate(action.value)
         is CreateAlertScreenActions.UpdateName -> updateName(action.value)
         is CreateAlertScreenActions.UpdateNotes -> updateNotes(action.value)
         CreateAlertScreenActions.Save -> save()
         is CreateAlertScreenActions.CreateReminder -> createReminder(action.value, action.range)
      }
   }

   private fun createReminder(value: Int, range: ReminderRange) {

      val existing = _reminders.value.filter { it.value == value && it.range == range }
      if (existing.any()) {
         viewModelScope.launch {
            _channel.send("Existing reminder found")
         }
      } else {
         _reminders.update {
            val list = it.toMutableList()
            list.add(
               Reminder(
                  id = UUID.randomUUID().toString(),
                  value = value,
                  range = range
               )
            )

            list.toImmutableList()
         }
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