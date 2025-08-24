package com.ns.expiration.expiration.alert.components.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ns.expiration.expiration.alert.components.LoadingAlert

@Composable
fun LoaderContainer(
   modifier: Modifier = Modifier,
   isLoading: Boolean,
   content: @Composable () -> Unit
) {
   Box(
      modifier = modifier,
      contentAlignment = Alignment.TopCenter
   ) {
      if (isLoading) {
         LoadingAlert()
      }
      content.invoke()
   }
}