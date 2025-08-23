package com.ns.expiration.expiration.alert.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.ns.expiration.expiration.alert.ui.theme.Background
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme
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
      shape = MaterialTheme.shapes.medium,
      colors = ButtonDefaults.buttonColors().copy(
         containerColor = Primary,
         contentColor = Background
      )
   ) {
      Text(text = text)
   }
}

@Preview
@Composable
private fun Preview() {
   ExpirationAlertTheme { 
      PrimaryButton(
         text = "Submit",
         onClick = {}
      )
   }
}