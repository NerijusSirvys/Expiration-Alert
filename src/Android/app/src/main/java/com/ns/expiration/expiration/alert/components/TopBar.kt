package com.ns.expiration.expiration.alert.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
   modifier: Modifier = Modifier,
   navigationIcon: Painter? = null,
   actions: @Composable RowScope.() -> Unit = {},
   onNavigation: () -> Unit = {},
   label: String,
) {
   TopAppBar(
      modifier = modifier,
      windowInsets = WindowInsets(0.dp),
      title = { Text(text = label) },
      actions = actions,
      navigationIcon = {
         navigationIcon?.let {
            IconButton(onClick = onNavigation) {
               Icon(
                  painter = navigationIcon,
                  contentDescription = "Return icon button"
               )
            }
         }
      }
   )
}