package com.ns.expiration.expiration.alert.screens.create

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.ns.expiration.expiration.alert.screens.create.data.AlertScreenTabs
import com.ns.expiration.expiration.alert.screens.create.data.tabs
import com.ns.expiration.expiration.alert.screens.create.tabContent.BasicInfoTabContent
import com.ns.expiration.expiration.alert.screens.create.tabContent.RemindersTabContent
import com.ns.expiration.expiration.alert.ui.theme.SunRay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlertScreenContent(
   modifier: Modifier = Modifier,
   state: CreateNewAlertScreenState,
   onAction: (CreateAlertScreenActions) -> Unit,
   onNavigateBack: () -> Unit,
) {
   Scaffold(
      modifier = modifier,
      topBar = {
         TopBar(
            label = "New Alert",
            navigationIcon = painterResource(R.drawable.ic_back_arrow),
            onNavigation = onNavigateBack,
            actions = {
               IconButton(onClick = { onAction.invoke(CreateAlertScreenActions.Save) }) {
                  Icon(
                     painter = painterResource(R.drawable.ic_save),
                     contentDescription = "Save alert icon"
                  )
               }
            }
         )
      }
   ) { innerPadding ->
      Column {
         var selectedTab by remember { mutableStateOf(AlertScreenTabs.BasicInfo) }
         TabRow(
            selectedTabIndex = selectedTab.ordinal,
            modifier = modifier.padding(innerPadding),
            indicator = {
               TabRowDefaults.SecondaryIndicator(
                  modifier = Modifier.tabIndicatorOffset(it[selectedTab.ordinal]),
                  color = SunRay
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
                  expirationDate = state.expirationDate,
                  onNameChange = { onAction.invoke(CreateAlertScreenActions.UpdateName(it)) },
                  onNotesChange = { onAction.invoke(CreateAlertScreenActions.UpdateNotes(it)) },
                  onExpirationSet = { onAction.invoke(CreateAlertScreenActions.SetExpirationDate(it)) }
               )

               AlertScreenTabs.Reminders -> RemindersTabContent()
               AlertScreenTabs.Picture -> Text(text = "Picture Screen")
            }
         }
      }
   }
}