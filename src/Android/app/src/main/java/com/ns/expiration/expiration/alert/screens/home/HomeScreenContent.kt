package com.ns.expiration.expiration.alert.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.CenteredMessage
import com.ns.expiration.expiration.alert.components.PainterIcon
import com.ns.expiration.expiration.alert.components.buttons.FloatingActionButton
import com.ns.expiration.expiration.alert.components.textFields.AppTextField
import com.ns.expiration.expiration.alert.screens.home.components.alert.AlertCard

@Composable
fun HomeScreenContent(
   modifier: Modifier = Modifier,
   state: HomeScreenState,
   onAction: (HomeScreenAction) -> Unit,
   onNavigateToNewAlert: () -> Unit,
   onNavigateToDetails: (String) -> Unit
) {
   Scaffold(
      containerColor = MaterialTheme.colorScheme.background,
      contentWindowInsets = WindowInsets(0.dp),
      floatingActionButton = {
         FloatingActionButton(
            text = "New Alert",
            onClick = onNavigateToNewAlert
         )
      }
   ) { innerPadding ->
      Column(
         modifier = modifier.padding(innerPadding),
         verticalArrangement = Arrangement.Top
      ) {
         AppTextField(
            state = state.searchTerm,
            onValueChange = { onAction.invoke(HomeScreenAction.UpdateSearchTerm(it)) },
            placeholder = { Text(text = "Search") },
            leadingIcon = {
               PainterIcon(
                  iconId = R.drawable.ic_search,
                  contentDescription = "Search Field Icon",
                  tint = MaterialTheme.colorScheme.onPrimary
               )
            },
            keyboardOptions = KeyboardOptions(
               showKeyboardOnFocus = true,
               autoCorrectEnabled = true
            )
         )

         Spacer(modifier = Modifier.height(25.dp))

         LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
         ) {

            if (state.alerts.isEmpty()) {
               item { CenteredMessage(text = "No Alerts Found") }
            } else {
               items(state.alerts, key = { alert -> alert.id }) { alert ->
                  AlertCard(
                     imageUrl = alert.image,
                     name = alert.name,
                     quantity = alert.quantity,
                     expiration = alert.expiration,
                     reminders = alert.reminders,
                     onClick = { onNavigateToDetails.invoke(alert.id) }
                  )
               }
            }
         }
      }
   }
}