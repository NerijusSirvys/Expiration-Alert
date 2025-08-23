package com.ns.expiration.expiration.alert.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
   background = Background,
   surface = Background,
   primary = Primary,
   primaryContainer = Primary,
   onPrimaryContainer = Color.Green,
   inversePrimary = Color.Green,
   onSecondaryContainer = Color.Green,
   onPrimary = OnPrimary,
   error = RedSalsa
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