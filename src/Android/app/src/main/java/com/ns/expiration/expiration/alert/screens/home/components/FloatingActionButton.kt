package com.ns.expiration.expiration.alert.screens.home.components

import android.content.res.Configuration
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ns.expiration.expiration.alert.ui.theme.CharcoalGrey
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme
import com.ns.expiration.expiration.alert.ui.theme.SunRay

@Composable
fun FloatingActionButton(
   modifier: Modifier = Modifier,
   text: String,
   onClick: () -> Unit
) {
   ExtendedFloatingActionButton(
      modifier = modifier,
      onClick = onClick,
      containerColor = SunRay,
      contentColor = CharcoalGrey
   ) {
      Text(text = text)
   }
}

@Preview(device = Devices.PIXEL, uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)

@Composable
private fun Preview() {
   ExpirationAlertTheme {
      FloatingActionButton(text = "New Reminder", onClick = {})
   }
}