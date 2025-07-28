package com.ns.expiration.expiration.alert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ns.expiration.expiration.alert.navigation.Destinations
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme

class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         ExpirationAlertTheme {
            val navController = rememberNavController()
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
               NavHost(modifier = Modifier.padding(innerPadding), navController = navController, startDestination = Destinations.Home) {
                  composable<Destinations.Home> { }
                  composable<Destinations.NewAlert> { }
               }
            }
         }
      }
   }
}