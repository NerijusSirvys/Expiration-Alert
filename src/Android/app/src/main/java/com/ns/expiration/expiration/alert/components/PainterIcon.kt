package com.ns.expiration.expiration.alert.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.ns.expiration.expiration.alert.ui.theme.White

@Composable
fun PainterIcon(
   modifier: Modifier = Modifier,
   @DrawableRes iconId: Int,
   contentDescription: String
) {
   Icon(
      modifier = modifier,
      painter = painterResource(iconId),
      contentDescription = contentDescription,
      tint = White.copy(alpha = 0.2f)
   )
}