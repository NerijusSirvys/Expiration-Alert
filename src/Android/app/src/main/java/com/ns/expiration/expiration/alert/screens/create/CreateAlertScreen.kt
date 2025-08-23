package com.ns.expiration.expiration.alert.screens.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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

   ObserveAsEvents(vm.messageChannel) {
      scope.launch {
         snackbarHostState.showSnackbar(it)
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