package com.ns.expiration.expiration.alert.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ns.expiration.expiration.alert.navigation.Destinations
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AlertDetailsScreen(
   modifier: Modifier = Modifier,
   navController: NavHostController
) {
   val details = navController.currentBackStackEntry?.toRoute<Destinations.AlertDetails>()
   val vm = koinViewModel<AlertDetailsScreenViewmodel> { parametersOf(details?.id ?: "") }
   val state by vm.state.collectAsStateWithLifecycle()

   AlertDetailsScreenContent(
      modifier = modifier,
      state = state,
      onAction = vm::onAction,
      onNavigateBack = {
         navController.navigateUp()
      },
   )
}