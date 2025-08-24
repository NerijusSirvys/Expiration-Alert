package com.ns.expiration.expiration.alert.utilities

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Modifier.maxHeight(float: Float): Modifier {

   val screenHeight = LocalConfiguration.current.screenHeightDp
   val maxHeight = screenHeight * float

   return this then Modifier
      .wrapContentHeight()
      .heightIn(max = maxHeight.dp)
}