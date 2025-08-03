package com.ns.expiration.expiration.alert.screens.home.components.alert

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.ui.theme.SlateGrey
import com.ns.expiration.expiration.alert.ui.theme.SunRay
import com.ns.expiration.expiration.alert.ui.theme.White

data class AlertCardOptions(
   val imageAreaWidth: Dp = 75.dp,
   val height: Dp = 75.dp,
   val loaderColor: Color = SunRay,
   val contentColor: Color = White,
   val color: Color = SlateGrey
) {
   companion object {
      @Stable
      val Default = AlertCardOptions()
   }
}
