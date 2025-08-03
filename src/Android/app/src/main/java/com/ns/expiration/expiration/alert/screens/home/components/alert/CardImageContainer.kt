package com.ns.expiration.expiration.alert.screens.home.components.alert

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CardImageContainer(
   modifier: Modifier = Modifier,
   areaWidth: Dp = 75.dp,
   content: @Composable () -> Unit
) {
   Box(
      contentAlignment = Alignment.Center,
      modifier = modifier
         .width(areaWidth)
   ) {

      content.invoke()
      VerticalDivider(modifier.offset(x = (areaWidth.value / 2).dp))
   }
}