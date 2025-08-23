package com.ns.expiration.expiration.alert.screens.create.tabContent.picture

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.PainterIcon
import com.ns.expiration.expiration.alert.components.buttons.PrimaryButton
import com.ns.expiration.expiration.alert.ui.theme.White

@Composable
fun PictureTabContent(modifier: Modifier = Modifier) {
   Column(
      modifier = modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Box(
         modifier = modifier.weight(1f)
      ) {
         PainterIcon(
            iconId = R.drawable.ic_add_camera,
            contentDescription = "Camera Icon",
            tint = White.copy(alpha = 0.05f),
            modifier = modifier
               .fillMaxSize()
               .padding(80.dp)
         )
      }

      PrimaryButton(
         text = "Take Image",
         onClick = {}
      )
   }
}