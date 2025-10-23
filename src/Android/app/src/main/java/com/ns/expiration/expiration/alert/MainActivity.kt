package com.ns.expiration.expiration.alert

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ns.expiration.expiration.alert.navigation.Destinations
import com.ns.expiration.expiration.alert.screens.authentication.AuthenticationScreen
import com.ns.expiration.expiration.alert.screens.details.AlertDetailsScreen
import com.ns.expiration.expiration.alert.screens.home.HomeScreen
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreen
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         ExpirationAlertTheme {
            val navController = rememberNavController()
            val snackBarHostState = remember { SnackbarHostState() }
            val googleClient = koinInject<GoogleSignInClient>()

            if (!hasRequiredPermissions()) {
               LocalActivity.current?.let { activity ->
                  ActivityCompat.requestPermissions(
                     activity, PERMISSIONS, 0
                  )
               }
            }

            googleClient.setupTokenRefreshListener(onRefreshFailed = {
               navController.navigate(Destinations.Authentication) {
                  this.popUpTo(Destinations.Authentication) {
                     inclusive = true
                  }
               }
            })

            val destination = if (googleClient.signedIn()) Destinations.Home
            else Destinations.Authentication

            Scaffold(
               modifier = Modifier.fillMaxSize(),
               snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) { innerPadding ->
               NavHost(
                  modifier = Modifier
                     .padding(innerPadding)
                     .padding(horizontal = 15.dp),
                  navController = navController,
                  startDestination = destination
               ) {
                  composable<Destinations.Home> {
                     HomeScreen(
                        navController = navController,
                     )
                  }
                  composable<Destinations.AlertDetails> {
                     AlertDetailsScreen(
                        navController = navController,
                        snackbarHostState = snackBarHostState,
                     )
                  }
                  composable<Destinations.ManageAlert> {
                     ManageAlertScreen(
                        navController = navController,
                        snackbarHostState = snackBarHostState
                     )
                  }
                  composable<Destinations.Authentication> {
                     AuthenticationScreen(
                        navController = navController,
                        snackbarHostState = snackBarHostState,
                        googleSignInClient = googleClient
                     )
                  }
               }
            }
         }
      }
   }

   fun hasRequiredPermissions(): Boolean {
      return PERMISSIONS.all {
         ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
      }
   }

   companion object {
      private val PERMISSIONS = arrayOf(
         Manifest.permission.CAMERA,
         Manifest.permission.POST_NOTIFICATIONS,
      )
   }
}
