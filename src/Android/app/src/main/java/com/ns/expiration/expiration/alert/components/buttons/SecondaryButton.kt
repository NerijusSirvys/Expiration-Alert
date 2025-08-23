package com.ns.expiration.expiration.alert.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme

@Composable
fun SecondaryButton(
   modifier: Modifier = Modifier,
   text: String,
   onClick: () -> Unit
) {
   OutlinedButton(
      onClick = onClick,
      modifier = modifier.fillMaxWidth(),
      shape = MaterialTheme.shapes.medium,
      border = ButtonDefaults.outlinedButtonBorder().copy(
         brush = SolidColor(MaterialTheme.colorScheme.error)
      ),
      colors = ButtonDefaults.outlinedButtonColors(
         contentColor = MaterialTheme.colorScheme.error
      )
   ) {
      Text(text = text)
   }
}

@Preview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      SecondaryButton(
         text = "Submit",
         onClick = {}
      )
   }
}