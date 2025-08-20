package com.ns.expiration.expiration.alert.screens.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun CreateAlertScreen(
   modifier: Modifier = Modifier,
   navHostController: NavHostController,
   snackbarHostState: SnackbarHostState
) {
   CreateAlertScreenContent(
      modifier = modifier,
      onNavigateBack = {
         navHostController.navigateUp()
      }
   )
}