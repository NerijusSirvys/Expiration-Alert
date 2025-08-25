package com.ns.expiration.expiration.alert.screens.manage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ns.expiration.expiration.alert.components.textFields.TextFieldState
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import com.ns.expiration.expiration.alert.repositories.data.Reminder
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import com.ns.expiration.expiration.alert.utilities.DateTimeHelpers
import com.ns.expiration.expiration.alert.utilities.toWebPStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import java.io.File
import java.util.UUID
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ManageAlertScreenViewmodel(
   val alertId: String? = null,
   val repository: AlertRepository,
   val context: Context,
) : ViewModel() {

   private val _channel = Channel<CreateAlertScreenEvents>()
   val messageChannel = _channel.receiveAsFlow()

   private val _state = MutableStateFlow(ManageAlertScreenState())
   val state = _state.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = ManageAlertScreenState()
   )

   init {
      if (alertId != null) {
         viewModelScope.launch {
            repository.getAlertById(alertId).collect { alertDetails ->

               val bitmap: Bitmap? = BitmapFactory.decodeFile(alertDetails.imageUrl)
               _state.update {
                  it.copy(
                     name = TextFieldState(alertDetails.name, true),
                     notes = TextFieldState(alertDetails.notes, true),
                     quantity = TextFieldState(alertDetails.quantity.toString(), true),
                     expirationDate = TextFieldState(alertDetails.expirationDate, true),
                     isLoading = false,
                     reminders = alertDetails.reminders,
                     image = bitmap
                  )
               }
            }
         }
      }
   }

   fun onAction(action: ManageAlertScreenActions) {
      when (action) {
         is ManageAlertScreenActions.ManageReminder -> createReminder(action.value, action.range)
         is ManageAlertScreenActions.SetExpirationDate -> setExpirationDate(action.value)
         is ManageAlertScreenActions.UpdateQuantity -> updateQuantity(action.value)
         is ManageAlertScreenActions.TakePicture -> takePicture(action.bitmap)
         is ManageAlertScreenActions.UpdateNotes -> updateNotes(action.value)
         is ManageAlertScreenActions.UpdateName -> updateName(action.value)
         ManageAlertScreenActions.ResetPicture -> resetPhoto()
         ManageAlertScreenActions.Save -> save()
         is ManageAlertScreenActions.DeleteReminder -> deleteReminder(action.id)
      }
   }

   private fun deleteReminder(id: String) {
      _state.update {
         val reminders = _state.value.reminders.toMutableList()
         reminders.removeAll { it.id == id }

         it.copy(reminders = reminders.toImmutableList())
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
      viewModelScope.launch {
         _state.update { it.copy(isLoading = true) }
         val state = _state.value
         if (!state.name.isValid || !state.quantity.isValid || !state.notes.isValid || !state.expirationDate.isValid) {
            _channel.send(CreateAlertScreenEvents.MissingInformation)
            _state.update { it.copy(isLoading = false) }
         } else {
            try {
               val nameIsValid = _state.value.name.value.isNotEmpty()
               val quantityIsValid = _state.value.quantity.value.isNotEmpty()
               val expirationIsValid = _state.value.expirationDate.value.isNotEmpty()
               if (!nameIsValid || !quantityIsValid || !expirationIsValid) {
                  _channel.send(CreateAlertScreenEvents.MissingInformation)
                  _state.update { it.copy(isLoading = false) }
               } else {

                  val id = alertId ?: UUID.randomUUID().toString()

                  val imageFileName = "${state.name.value}_${id}.webp"
                  withContext(Dispatchers.IO) {
                     val stream = state.image?.toWebPStream()

                     val path = stream?.let { bytes ->
                        val file = File(context.filesDir, imageFileName)
                        file.outputStream().use { it.write(bytes) }
                        file.absolutePath
                     } ?: ""

                     repository.saveAlert(id, path, state)
                  }
                  _channel.send(CreateAlertScreenEvents.AlertSavedSuccess)
               }
            } catch (e: Exception) {
               Log.e(ManageAlertScreenViewmodel::class.qualifiedName, "Failed to save the alert", e)
               _state.update { it.copy(isLoading = false) }
               _channel.send(CreateAlertScreenEvents.AlertSaveFailed)
            }
         }
      }
   }

   private fun setExpirationDate(value: String) {
      val dateAsText = DateTimeHelpers.convertMillisToDate(value.toLong())
      _state.update { it.copy(expirationDate = TextFieldState(dateAsText, true)) }
   }

   private fun updateNotes(value: String) {
      _state.update { it.copy(notes = TextFieldState(value, true)) }
   }

   private fun updateName(value: String) {
      val isValid = !value.isEmpty()
      _state.update { it.copy(name = TextFieldState(value, isValid)) }
   }

   private fun updateQuantity(value: String) {
      val isValid = value.toIntOrNull() != null
      _state.update { it.copy(quantity = TextFieldState(value, isValid)) }
   }

   private fun resetPhoto() {
      _state.update { it.copy(image = null) }
   }

   private fun takePicture(bitmap: Bitmap) {
      _state.update { it.copy(image = bitmap) }
   }
}