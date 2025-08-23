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
import com.ns.expiration.expiration.alert.screens.create.CreateAlertScreen
import com.ns.expiration.expiration.alert.screens.details.AlertDetailsScreen
import com.ns.expiration.expiration.alert.screens.home.HomeScreen
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme

class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         ExpirationAlertTheme {
            val navController = rememberNavController()
            val snackBarHostState = remember { SnackbarHostState() }

            Scaffold(
               modifier = Modifier.fillMaxSize(),
               snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
            ) { innerPadding ->
               NavHost(
                  modifier = Modifier
                     .padding(innerPadding)
                     .padding(horizontal = 15.dp),
                  navController = navController,
                  startDestination = Destinations.Home
               ) {
                  composable<Destinations.Home> { HomeScreen(navController = navController) }
                  composable<Destinations.AlertDetails> {
                     AlertDetailsScreen(
                        navController = navController,
                        snackbarHostState = snackBarHostState,
                     )
                  }
                  composable<Destinations.NewAlert> {
                     if (!hasRequiredPermissions()) {
                        LocalActivity.current?.let { activity ->
                           ActivityCompat.requestPermissions(
                              activity, CAMERAX_PERMISSIONS, 0
                           )
                        }
                     }

                     CreateAlertScreen(
                        navHostController = navController,
                        snackbarHostState = snackBarHostState
                     )
                  }
               }
            }
         }
      }
   }

   fun hasRequiredPermissions(): Boolean {
      return CAMERAX_PERMISSIONS.all {
         ContextCompat.checkSelfPermission(
            applicationContext,
            it
         ) == PackageManager.PERMISSION_GRANTED
      }
   }

   companion object {
      private val CAMERAX_PERMISSIONS = arrayOf(
         Manifest.permission.CAMERA,
      )
   }
}