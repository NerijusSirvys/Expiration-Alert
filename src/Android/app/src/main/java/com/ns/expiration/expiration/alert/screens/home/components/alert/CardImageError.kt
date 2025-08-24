package com.ns.expiration.expiration.alert.screens.home.components.alert

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CardImageError(
   modifier: Modifier = Modifier,
   text: String
) {
   CardImageContainer(
      modifier = modifier.padding(horizontal = 2.dp)
   ) {
      Text(
         text = text,
         textAlign = TextAlign.Center
      )
   }
}