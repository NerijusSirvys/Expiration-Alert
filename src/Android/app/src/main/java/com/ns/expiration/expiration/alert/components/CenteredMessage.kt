package com.ns.expiration.expiration.alert.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CenteredMessage(
   modifier: Modifier = Modifier,
   text: String
) {
   Text(
      text = text,
      modifier = modifier.fillMaxWidth(),
      textAlign = TextAlign.Center
   )
}