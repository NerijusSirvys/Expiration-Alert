package com.ns.expiration.expiration.alert.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ns.expiration.expiration.alert.ui.theme.Background
import com.ns.expiration.expiration.alert.ui.theme.Primary

@Composable
fun PrimaryButton(
   modifier: Modifier = Modifier,
   text: String,
   onClick: () -> Unit
) {
   Button(
      onClick = onClick,
      modifier = modifier.fillMaxWidth(),
      colors = ButtonDefaults.buttonColors().copy(
         containerColor = Primary,
         contentColor = Background
      )
   ) {
      Text(text = text)
   }
}