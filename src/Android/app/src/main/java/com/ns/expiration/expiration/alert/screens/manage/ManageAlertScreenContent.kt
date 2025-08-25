package com.ns.expiration.expiration.alert.screens.manage

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.TopBar
import com.ns.expiration.expiration.alert.screens.create.tabContent.basicInfo.BasicInfoTabContent
import com.ns.expiration.expiration.alert.screens.create.tabContent.picture.PictureTabContent
import com.ns.expiration.expiration.alert.screens.create.tabContent.reminders.RemindersTabContent
import com.ns.expiration.expiration.alert.screens.manage.data.AlertScreenTabs
import com.ns.expiration.expiration.alert.screens.manage.data.ManageAlertType
import com.ns.expiration.expiration.alert.screens.manage.data.tabs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAlertScreenContent(
   modifier: Modifier = Modifier,
   state: ManageAlertScreenState,
   type: ManageAlertType,
   onAction: (ManageAlertScreenActions) -> Unit,
   onNavigateBack: () -> Unit,
) {
   Scaffold(
      modifier = modifier,
      topBar = {
         TopBar(
            label = when (type) {
               ManageAlertType.Create -> "New Alert"
               ManageAlertType.Edit -> "Edit Alert"
            },
            navigationIcon = painterResource(R.drawable.ic_back_arrow),
            onNavigation = onNavigateBack,
            actions = {
               IconButton(onClick = { onAction.invoke(ManageAlertScreenActions.Save) }) {
                  Icon(
                     painter = painterResource(R.drawable.ic_save),
                     contentDescription = "Save alert icon"
                  )
               }
            }
         )
      }
   ) { innerPadding ->
      Column(
         modifier.padding(innerPadding)
      ) {
         var selectedTab by remember { mutableStateOf(AlertScreenTabs.BasicInfo) }
         TabRow(
            selectedTabIndex = selectedTab.ordinal,
            indicator = {
               TabRowDefaults.SecondaryIndicator(
                  modifier = Modifier.tabIndicatorOffset(it[selectedTab.ordinal]),
                  color = MaterialTheme.colorScheme.primary
               )
            }
         ) {
            tabs.forEach {
               val selected = it.type == selectedTab

               Tab(
                  selected = selected,
                  onClick = { selectedTab = it.type },
                  text = { Text(it.label) },
                  selectedContentColor = if (selected) it.selectedColor
                  else it.unselectedColor,
               )
            }
         }

         AnimatedContent(
            targetState = selectedTab,
            contentAlignment = Alignment.TopCenter
         ) { targetState ->
            when (targetState) {
               AlertScreenTabs.BasicInfo -> BasicInfoTabContent(
                  name = state.name,
                  notes = state.notes,
                  quantity = state.quantity,
                  expirationDate = state.expirationDate,
                  onNameChange = { onAction.invoke(ManageAlertScreenActions.UpdateName(it)) },
                  onNotesChange = { onAction.invoke(ManageAlertScreenActions.UpdateNotes(it)) },
                  onExpirationSet = { onAction.invoke(ManageAlertScreenActions.SetExpirationDate(it)) },
                  onQuantityChange = { onAction.invoke(ManageAlertScreenActions.UpdateQuantity(it)) }
               )

               AlertScreenTabs.Reminders -> RemindersTabContent(
                  reminders = state.reminders,
                  onReminderRemove = { onAction.invoke(ManageAlertScreenActions.DeleteReminder(it)) },
                  onReminderCreate = { value, range ->
                     onAction.invoke(ManageAlertScreenActions.ManageReminder(value, range))
                  }
               )

               AlertScreenTabs.Picture -> PictureTabContent(
                  image = state.image,
                  onPhotoTaken = { onAction.invoke(ManageAlertScreenActions.TakePicture(it)) },
                  onResetPhoto = { onAction.invoke(ManageAlertScreenActions.ResetPicture) },
               )
            }
         }
      }
   }
}