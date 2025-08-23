package com.ns.expiration.expiration.alert.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
   background = Background,
   surface = Background,
)

@Composable
fun ExpirationAlertTheme(
   content: @Composable () -> Unit
) {
   MaterialTheme(
      colorScheme = DarkColorScheme,
      typography = Typography,
      content = content
   )
}