package com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange

@Composable
fun ReminderRangeChipRow(
   modifier: Modifier = Modifier,
   selected: ReminderRange,
   onSelectionChange: (ReminderRange) -> Unit
) {
   Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
   ) {
      ReminderRangeChip(
         selected = selected == ReminderRange.Days,
         onClick = { onSelectionChange.invoke(ReminderRange.Days) },
         range = ReminderRange.Days
      )

      ReminderRangeChip(
         selected = selected == ReminderRange.Weeks,
         onClick = { onSelectionChange.invoke(ReminderRange.Weeks) },
         range = ReminderRange.Weeks
      )

      ReminderRangeChip(
         selected = selected == ReminderRange.Months,
         onClick = { onSelectionChange.invoke(ReminderRange.Months) },
         range = ReminderRange.Months
      )
   }
}