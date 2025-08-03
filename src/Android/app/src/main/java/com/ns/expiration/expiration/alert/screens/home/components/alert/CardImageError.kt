package com.ns.expiration.expiration.alert.screens.home.components.alert

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CardImageError(
   modifier: Modifier = Modifier,
   text: String
) {
   CardImageContainer(
      modifier = modifier
   ) {
      Text(
         text = text,
         textAlign = TextAlign.Center
      )
   }
}