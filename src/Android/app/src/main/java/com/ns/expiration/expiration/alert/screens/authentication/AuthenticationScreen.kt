package com.ns.expiration.expiration.alert.screens.authentication

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ns.expiration.expiration.alert.GoogleSignInClient
import com.ns.expiration.expiration.alert.components.containers.LoaderContainer
import com.ns.expiration.expiration.alert.navigation.Destinations
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
   modifier: Modifier = Modifier,
   navController: NavHostController,
   snackbarHostState: SnackbarHostState,
   googleSignInClient: GoogleSignInClient
) {

   val scope = rememberCoroutineScope()

   var isLoading by remember { mutableStateOf(false) }

   LoaderContainer(
      isLoading = isLoading
   ) {
      AuthenticationScreenContent(
         modifier = modifier,
         onAuthenticate = {
            isLoading = true
            scope.launch {
               googleSignInClient.signIn(
                  onError = {
                     scope.launch {
                        snackbarHostState.showSnackbar(it)
                        isLoading = false
                     }
                  },
                  onSuccess = {
                     navController.navigate(Destinations.Home) {
                        popUpTo(Destinations.Authentication) {
                           inclusive = true
                        }
                     }
                     isLoading = false
                  }
               )
            }
         }
      )
   }
}