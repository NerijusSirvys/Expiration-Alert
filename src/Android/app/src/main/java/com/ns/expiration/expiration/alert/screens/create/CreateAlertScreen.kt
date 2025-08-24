package com.ns.expiration.expiration.alert.screens.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ns.expiration.expiration.alert.utilities.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateAlertScreen(
   modifier: Modifier = Modifier,
   navHostController: NavHostController,
   snackbarHostState: SnackbarHostState
) {

   val vm = koinViewModel<CreateAlertScreenViewmodel>()
   val state by vm.state.collectAsStateWithLifecycle()
   val scope = rememberCoroutineScope()
   val keyboardController = LocalSoftwareKeyboardController.current

   ObserveAsEvents(vm.messageChannel) {
      scope.launch {
         keyboardController?.hide()
         when (it) {
            CreateAlertScreenEvents.AlertSaveFailed -> {
               snackbarHostState.showSnackbar("Failed to create alert")
            }

            CreateAlertScreenEvents.AlertSavedSuccess -> {
               navHostController.navigateUp()
               snackbarHostState.showSnackbar("Alert created")
            }

            CreateAlertScreenEvents.ReminderDuplicateFound -> {
               snackbarHostState.showSnackbar("Reminder already exist")
            }

            CreateAlertScreenEvents.MissingInformation -> {
               snackbarHostState.showSnackbar("Missing information")
            }
         }
      }
   }

   CreateAlertScreenContent(
      modifier = modifier,
      state = state,
      onAction = vm::onAction,
      onNavigateBack = {
         navHostController.navigateUp()
      }
   )
}