package com.ns.expiration.expiration.alert.screens.home

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.ns.expiration.expiration.alert.navigation.Destinations
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
   modifier: Modifier = Modifier,
   navController: NavHostController,
   drawerState: DrawerState
) {
   val vm = koinViewModel<HomeScreenViewmodel>()
   val state by vm.state.collectAsStateWithLifecycle()
   val scope = rememberCoroutineScope()

   HomeScreenContent(
      modifier = modifier,
      state = state,
      onAction = vm::onAction,
      onNavigateToNewAlert = {
         navController.navigate(Destinations.ManageAlert(null))
      },
      onNavigateToDetails = {
         navController.navigate(Destinations.AlertDetails(it))
      },
      onMenuToggle = {
         scope.launch {
            if (drawerState.isClosed) {
               drawerState.open()
            }
         }
      }
   )
}