package com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ns.expiration.expiration.alert.repositories.data.ReminderRange
import com.ns.expiration.expiration.alert.ui.theme.Background
import com.ns.expiration.expiration.alert.ui.theme.Primary

@Composable
fun ReminderRangeChip(
   modifier: Modifier = Modifier,
   range: ReminderRange,
   selected: Boolean,
   onClick: () -> Unit
) {
   FilterChip(
      modifier = modifier,
      selected = selected,
      onClick = onClick,
      label = { Text(range.name) },
      colors = FilterChipDefaults.filterChipColors(
         selectedContainerColor = Primary,
         selectedLabelColor = Background
      )
   )
}