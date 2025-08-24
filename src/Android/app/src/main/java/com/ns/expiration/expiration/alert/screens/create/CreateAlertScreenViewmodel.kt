package com.ns.expiration.expiration.alert.screens.create

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import com.ns.expiration.expiration.alert.screens.create.data.Reminder
import com.ns.expiration.expiration.alert.utilities.toWebPStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import java.io.File
import java.util.UUID
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CreateAlertScreenViewmodel(
   val repository: AlertRepository,
   val context: Context
) : ViewModel() {

   private val _channel = Channel<CreateAlertScreenEvents>()
   val messageChannel = _channel.receiveAsFlow()

   private val _state = MutableStateFlow(CreateNewAlertScreenState())
   val state = _state.asStateFlow()

   fun onAction(action: CreateAlertScreenActions) {
      when (action) {
         is CreateAlertScreenActions.CreateReminder -> createReminder(action.value, action.range)
         is CreateAlertScreenActions.SetExpirationDate -> setExpirationDate(action.value)
         is CreateAlertScreenActions.UpdateQuantity -> updateQuantity(action.value)
         is CreateAlertScreenActions.TakePicture -> takePicture(action.bitmap)
         is CreateAlertScreenActions.UpdateNotes -> updateNotes(action.value)
         is CreateAlertScreenActions.UpdateName -> updateName(action.value)
         CreateAlertScreenActions.ResetPicture -> resetPhoto()
         CreateAlertScreenActions.Save -> save()
      }
   }

   private fun createReminder(value: Int, range: ReminderRange) {
      _state.update { state ->
         val mutableReminderList = state.reminders.toMutableList()

         val existing = mutableReminderList.filter { it.value == value && it.range == range }
         if (existing.any()) {
            viewModelScope.launch {
               _channel.send(CreateAlertScreenEvents.ReminderDuplicateFound)
            }
         } else {
            mutableReminderList.add(
               Reminder(
                  id = UUID.randomUUID().toString(),
                  value = value,
                  range = range
               )
            )
         }
         state.copy(
            reminders = mutableReminderList.toImmutableList()
         )
      }
   }

   private fun save() {
      try {
         val id = UUID.randomUUID().toString()
         val nowInMilliseconds = System.currentTimeMillis()

         val imageFileName = "${_state.value.name}_${nowInMilliseconds}_${id}"
         val stream = _state.value.image?.toWebPStream()
         val file = File(context.filesDir, imageFileName)
         file.outputStream().use { it.write(stream) }

         viewModelScope.launch(Dispatchers.IO) {
            repository.saveAlert(id, file.absolutePath, nowInMilliseconds, _state.value)
            _channel.send(CreateAlertScreenEvents.AlertSavedSuccess)
         }
      } catch (e: Exception) {
         Log.e(CreateAlertScreenViewmodel::class.qualifiedName, "Failed to save the alert", e)
         viewModelScope.launch {
            _channel.send(CreateAlertScreenEvents.AlertSaveFailed)
         }
      }
   }

   private fun setExpirationDate(value: Long) {
      _state.update { it.copy(expirationDate = value) }
   }

   private fun updateNotes(value: String) {
      _state.update { it.copy(notes = value) }
   }

   private fun updateName(value: String) {
      _state.update { it.copy(name = value) }
   }

   private fun updateQuantity(value: String) {
      _state.update { it.copy(quantity = value) }
   }

   private fun resetPhoto() {
      _state.update { it.copy(image = null) }
   }

   private fun takePicture(bitmap: Bitmap) {
      _state.update { it.copy(image = bitmap) }
   }
}