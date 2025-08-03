package com.ns.expiration.expiration.alert.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ns.expiration.expiration.alert.navigation.Destinations
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
   modifier: Modifier = Modifier,
   navController: NavHostController,
) {
   val vm = koinViewModel<HomeScreenViewmodel>()
   val state by vm.state.collectAsStateWithLifecycle()

   HomeScreenContent(
      modifier = modifier,
      state = state,
      onAction = vm::onAction,
      onNavigateToNewAlert = {
         navController.navigate(Destinations.NewAlert)
      },
      onNavigateToDetails = {
         println("Navigating to details. ID: $it")
      }
   )
}