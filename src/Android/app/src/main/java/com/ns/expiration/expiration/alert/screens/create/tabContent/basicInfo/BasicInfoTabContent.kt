package com.ns.expiration.expiration.alert.screens.create.tabContent.basicInfo

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.AppTextField
import com.ns.expiration.expiration.alert.components.DatePickerModal
import com.ns.expiration.expiration.alert.components.PainterIcon
import com.ns.expiration.expiration.alert.utilities.DateTimeHelpers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInfoTabContent(
   modifier: Modifier = Modifier,
   name: String,
   notes: String,
   quantity: String,
   expirationDate: Long?,
   onNameChange: (String) -> Unit,
   onNotesChange: (String) -> Unit,
   onQuantityChange: (String) -> Unit,
   onExpirationSet: (Long) -> Unit,
) {
   Column {
      AppTextField(
         value = name,
         onValueChange = { onNameChange.invoke(it) },
         label = { Text(text = "Name") }
      )

      AppTextField(
         value = quantity.toString(),
         onValueChange = { onQuantityChange.invoke(it) },
         label = { Text(text = "Quantity") },
         keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
         )
      )

      AppTextField(
         value = notes,
         onValueChange = { onNotesChange.invoke(it) },
         label = { Text(text = "Notes") },
         minLines = 4,
         maxLines = 4
      )

      Spacer(modifier.height(50.dp))

      Text(
         text = "Select expiration date",
         style = MaterialTheme.typography.headlineSmall
      )

      var showModal by remember { mutableStateOf(false) }

      AppTextField(
         value = expirationDate?.let { DateTimeHelpers.convertMillisToDate(it) } ?: "",
         onValueChange = { },
         label = { Text("Date") },
         placeholder = { Text("MM/DD/YYYY") },
         trailingIcon = {
            PainterIcon(
               iconId = R.drawable.ic_date_range,
               contentDescription = "Select date",
               tint = MaterialTheme.colorScheme.onPrimary
            )
         },
         modifier = Modifier
            .pointerInput(expirationDate) {
               awaitEachGesture {
                  awaitFirstDown(pass = PointerEventPass.Initial)
                  val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                  if (upEvent != null) {
                     showModal = true
                  }
               }
            }
      )

      if (showModal) {
         DatePickerModal(
            onDateSelected = {
               it?.let {
                  onExpirationSet.invoke(it)
               }
            },
            onDismiss = { showModal = false }
         )
      }
   }
}