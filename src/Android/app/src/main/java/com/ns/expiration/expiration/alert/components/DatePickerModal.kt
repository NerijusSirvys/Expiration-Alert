package com.ns.expiration.expiration.alert.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
   onDateSelected: (Long?) -> Unit,
   onDismiss: () -> Unit
) {

   val datePickerState = rememberDatePickerState(
      selectableDates = FutureOrPresentSelectableDates
   )

   DatePickerDialog(
      onDismissRequest = onDismiss,
      confirmButton = {
         TextButton(
            onClick = {
               onDateSelected(datePickerState.selectedDateMillis)
               onDismiss()
            }) {
            Text("OK")
         }
      },
      dismissButton = {
         TextButton(onClick = onDismiss) {
            Text("Cancel")
         }
      }
   ) {
      DatePicker(state = datePickerState)
   }
}

@OptIn(ExperimentalMaterial3Api::class)
object FutureOrPresentSelectableDates : SelectableDates {
   private val today = LocalDate.now()

   @OptIn(ExperimentalTime::class)
   override fun isSelectableDate(utcTimeMillis: Long): Boolean {

      val date = Instant.ofEpochMilli(utcTimeMillis)
         .atZone(ZoneId.systemDefault())
         .toLocalDate()

      return date >= today
   }

   override fun isSelectableYear(year: Int): Boolean {
      return year >= today.year
   }
}