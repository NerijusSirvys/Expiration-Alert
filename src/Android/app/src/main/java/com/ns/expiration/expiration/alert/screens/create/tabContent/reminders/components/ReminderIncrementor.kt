package com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.PainterIcon
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme
import com.ns.expiration.expiration.alert.ui.theme.White

@Composable
fun ReminderIncrementor(
   modifier: Modifier = Modifier,
   value: Int,
   onValueChange: (Int) -> Unit
) {
   Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(15.dp)
   ) {
      IconButton(
         onClick = {
            if (value - 1 > 0) {
               onValueChange.invoke(value - 1)
            }
         }
      ) {
         PainterIcon(
            iconId = R.drawable.ic_remove,
            contentDescription = "Minus Icon"
         )
      }
      Text(
         text = value.toString(),
         color = White
      )

      IconButton(
         onClick = { onValueChange.invoke(value + 1) }
      ) {
         PainterIcon(
            iconId = R.drawable.ic_add,
            contentDescription = "Plus Icon"
         )
      }
   }
}

@Preview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      ReminderIncrementor(
         value = 2,
         onValueChange = {}
      )
   }
}