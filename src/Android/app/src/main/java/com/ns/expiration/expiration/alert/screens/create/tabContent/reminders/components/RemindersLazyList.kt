package com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.screens.create.data.Reminder

@Composable
fun RemindersLazyList(
   modifier: Modifier = Modifier,
   reminders: List<Reminder>
) {
   if (reminders.any()) {

      LazyColumn(
         modifier = modifier,
         verticalArrangement = Arrangement.spacedBy(5.dp),
         contentPadding = PaddingValues(bottom = 15.dp)
      ) {
         items(reminders, key = { reminder -> reminder.id }) {
            Card(
               modifier = Modifier
                  .fillMaxWidth()
                  .animateItem(),
               colors = CardDefaults.cardColors(
                  containerColor = MaterialTheme.colorScheme.onPrimary
               )
            ) {
               Text(
                  text = it.toString(),
                  modifier = Modifier.padding(vertical = 15.dp, horizontal = 25.dp)
               )
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