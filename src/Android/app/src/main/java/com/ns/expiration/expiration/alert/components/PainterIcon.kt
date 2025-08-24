package com.ns.expiration.expiration.alert.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PainterIcon(
   modifier: Modifier = Modifier,
   @DrawableRes iconId: Int,
   contentDescription: String,
   tint: Color = Color.Unspecified,
   size: Dp = 24.dp
) {
   Icon(
      modifier = modifier.size(size),
      painter = painterResource(iconId),
      contentDescription = contentDescription,
      tint = tint
   )
}