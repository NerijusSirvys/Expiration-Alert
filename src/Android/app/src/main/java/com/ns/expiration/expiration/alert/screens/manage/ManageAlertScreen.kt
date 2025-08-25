package com.ns.expiration.expiration.alert.screens.manage

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ns.expiration.expiration.alert.components.containers.LoaderContainer
import com.ns.expiration.expiration.alert.navigation.Destinations
import com.ns.expiration.expiration.alert.screens.manage.data.ManageAlertType
import com.ns.expiration.expiration.alert.utilities.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ManageAlertScreen(
   modifier: Modifier = Modifier,
   navController: NavHostController,
   snackbarHostState: SnackbarHostState
) {
   val data = navController.currentBackStackEntry?.toRoute<Destinations.ManageAlert>()
   val vm = koinViewModel<ManageAlertScreenViewmodel> { parametersOf(data?.id) }
   val state by vm.state.collectAsStateWithLifecycle()
   val scope = rememberCoroutineScope()
   val keyboardController = LocalSoftwareKeyboardController.current

   ObserveAsEvents(vm.messageChannel) {
      scope.launch {
         keyboardController?.hide()
         when (it) {
            CreateAlertScreenEvents.AlertSaveFailed -> {
               val msg = when (getScreenType(data)) {
                  ManageAlertType.Create -> "Failed to create alert"
                  ManageAlertType.Edit -> "Failed to edit alert"
               }
               snackbarHostState.showSnackbar(msg)
            }

            CreateAlertScreenEvents.AlertSavedSuccess -> {
               val msg = when (getScreenType(data)) {
                  ManageAlertType.Create -> "Alert Created"
                  ManageAlertType.Edit -> "Alert Updated"
               }

               navController.navigateUp()
               snackbarHostState.showSnackbar(msg)
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

   LoaderContainer(isLoading = state.isLoading) {
      ManageAlertScreenContent(
         modifier = modifier,
         state = state,
         onAction = vm::onAction,
         type = getScreenType(data),
         onNavigateBack = {
            navController.navigateUp()
         }
      )
   }
}

fun getScreenType(data: Destinations.ManageAlert?): ManageAlertType {
   return if (data?.id == null)
      ManageAlertType.Create
   else ManageAlertType.Edit
}