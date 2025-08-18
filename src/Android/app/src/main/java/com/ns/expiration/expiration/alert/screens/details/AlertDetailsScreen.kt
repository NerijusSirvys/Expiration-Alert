package com.ns.expiration.expiration.alert.screens.details

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ns.expiration.expiration.alert.navigation.Destinations
import com.ns.expiration.expiration.alert.utilities.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AlertDetailsScreen(
   modifier: Modifier = Modifier,
   navController: NavHostController,
   snackbarHostState: SnackbarHostState,
) {
   val details = navController.currentBackStackEntry?.toRoute<Destinations.AlertDetails>()
   val vm = koinViewModel<AlertDetailsScreenViewmodel> { parametersOf(details?.id ?: "") }
   val state by vm.state.collectAsStateWithLifecycle()
   val scope = rememberCoroutineScope()

   ObserveAsEvents(vm.eventChannel) {
      scope.launch {
         when (it) {
            is AlertDetailScreenEvents.AlertComplete -> when (it.result) {
               AlertActionResult.Success -> {
                  navController.navigateUp()
                  snackbarHostState.showSnackbar(message = "Alert Completed")
               }

               AlertActionResult.Failed -> {
                  snackbarHostState.showSnackbar(message = "Operation Failed")
               }
            }

            is AlertDetailScreenEvents.AlertDelete -> when (it.result) {
               AlertActionResult.Success -> {
                  navController.navigateUp()
                  snackbarHostState.showSnackbar(message = "Alert Deleted")
               }

               AlertActionResult.Failed -> {
                  snackbarHostState.showSnackbar(message = "Operation Failed")
               }
            }
         }
      }
   }

   AlertDetailsScreenContent(
      modifier = modifier,
      state = state,
      onAction = vm::onAction,
      onNavigateBack = { navController.navigateUp() },
   )
}