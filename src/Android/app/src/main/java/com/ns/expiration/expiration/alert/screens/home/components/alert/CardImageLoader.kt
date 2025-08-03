package com.ns.expiration.expiration.alert.screens.home.components.alert

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CardImageLoader(
   modifier: Modifier = Modifier,
   areaWidth: Dp = 75.dp,
   color: Color
) {
   CardImageContainer(
      areaWidth = areaWidth,
      modifier = modifier
   ) {
      CircularProgressIndicator(
         color = color
      )
   }
}
