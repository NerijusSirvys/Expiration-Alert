package com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.PainterIcon
import com.ns.expiration.expiration.alert.components.buttons.AppTextButton
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme
import com.ns.expiration.expiration.alert.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDialog(
   modifier: Modifier = Modifier,
   onCreate: (Int, ReminderRange) -> Unit,
   onDismiss: () -> Unit = {}
) {
   var valueState by remember { mutableIntStateOf(1) }
   var rangeState by remember { mutableStateOf(ReminderRange.Days) }

   BasicAlertDialog(
      onDismissRequest = onDismiss,
   ) {
      Card(
      ) {
         Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
               .fillMaxWidth()
               .padding(25.dp)
         ) {

            PainterIcon(
               iconId = R.drawable.ic_alert,
               contentDescription = "Alert Icon",
               tint = White,
               size = 30.dp
            )

            Spacer(modifier.height(10.dp))

            Text(
               text = "New Reminder",
               style = MaterialTheme.typography.headlineSmall
            )
            ReminderRangeChipRow(
               modifier = modifier.padding(vertical = 5.dp),
               onSelectionChange = { rangeState = it },
               selected = rangeState
            )

            ReminderIncrementor(
               modifier = modifier.padding(vertical = 2.dp),
               value = valueState,
               onValueChange = { valueState = it }
            )

            Row(
               modifier = modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.End
            ) {
               AppTextButton(
                  onClick = onDismiss,
                  text = "Cancel"
               )

               AppTextButton(
                  onClick = { onCreate.invoke(valueState, rangeState) },
                  text = "Ok"
               )
            }
         }
      }
   }
}

@Preview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      ReminderDialog(
         onDismiss = {},
         onCreate = { _, _ -> }
      )
   }
}