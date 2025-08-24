package com.ns.expiration.expiration.alert.screens.create.tabContent.reminders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.ComponentPreview
import com.ns.expiration.expiration.alert.components.buttons.PrimaryButton
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import com.ns.expiration.expiration.alert.screens.create.data.Reminder
import com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.components.ReminderDialog
import com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.components.RemindersLazyList
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme
import com.ns.expiration.expiration.alert.utilities.maxHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersTabContent(
   modifier: Modifier = Modifier,
   onReminderCreate: (Int, ReminderRange) -> Unit,
   onReminderRemove: (String) -> Unit,
   reminders: List<Reminder>
) {
   var showReminderDialog by remember { mutableStateOf(false) }

   Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.padding(top = 10.dp),
   ) {
      RemindersLazyList(
         modifier = Modifier.maxHeight(0.75f),
         reminders = reminders,
         onItemDeleted = { onReminderRemove.invoke(it) }
      )

      HorizontalDivider()

      if (showReminderDialog) {
         ReminderDialog(
            onDismiss = { showReminderDialog = false },
            onCreate = { value, range ->
               onReminderCreate.invoke(value, range)
               showReminderDialog = false
            }
         )
      }

      PrimaryButton(
         modifier = modifier.padding(top = 10.dp),
         text = "Add Reminder",
         onClick = { showReminderDialog = true }
      )
   }
}

@ComponentPreview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      Box(
         contentAlignment = Alignment.Center,
         modifier = Modifier.fillMaxSize()
      ) {
         RemindersTabContent(
            onReminderCreate = { _, _ -> },
            onReminderRemove = {},
            reminders = listOf(
               Reminder(
                  id = "1",
                  range = ReminderRange.Days,
                  value = 3
               ),
               Reminder(
                  id = "2",
                  range = ReminderRange.Days,
                  value = 5
               ),
               Reminder(
                  id = "3",
                  range = ReminderRange.Weeks,
                  value = 1
               )
            )
         )
      }
   }
}