package com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.PainterIcon
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import com.ns.expiration.expiration.alert.screens.create.data.Reminder
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme
import com.ns.expiration.expiration.alert.ui.theme.White

@Composable
fun RemindersLazyList(
   modifier: Modifier = Modifier,
   onItemDeleted: (String) -> Unit,
   reminders: List<Reminder>
) {
   if (reminders.any()) {

      LazyColumn(
         modifier = modifier,
         verticalArrangement = Arrangement.spacedBy(5.dp),
         contentPadding = PaddingValues(bottom = 15.dp)
      ) {
         items(reminders, key = { reminder -> reminder.id }) { reminder ->
            Card(
               modifier = Modifier
                  .fillMaxWidth()
                  .animateItem(),
               colors = CardDefaults.cardColors(
                  containerColor = MaterialTheme.colorScheme.onPrimary
               )
            ) {
               Row(
                  modifier = modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
               ) {
                  Text(
                     text = reminder.toString(),
                     modifier = Modifier.padding(vertical = 15.dp, horizontal = 25.dp)
                  )

                  IconButton(onClick = { onItemDeleted.invoke(reminder.id) }) {
                     PainterIcon(
                        iconId = R.drawable.ic_bin,
                        contentDescription = "Bin Icon",
                        tint = White
                     )
                  }
               }
            }
         }
      }
   } else {
      Text(
         textAlign = TextAlign.Center,
         text = "No Reminders Found",
         modifier = Modifier.padding(vertical = 15.dp)
      )
   }
}

@Preview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      RemindersLazyList(
         reminders = listOf(
            Reminder(
               id = "1",
               range = ReminderRange.Days,
               value = 2
            )
         ),
         onItemDeleted = {}
      )
   }
}