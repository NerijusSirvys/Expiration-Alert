package com.ns.expiration.expiration.alert.screens.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.PainterIcon
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme

@Composable
fun AuthenticationScreenContent(
   modifier: Modifier = Modifier,
   onAuthenticate: () -> Unit
) {
   Column(
      modifier = modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {

      Column(
         modifier = modifier
            .fillMaxWidth()
            .weight(1f),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Bottom
      ) {
         Row {
            PainterIcon(
               contentDescription = "",
               iconId = R.drawable.ic_calendar,
               tint = MaterialTheme.colorScheme.primary,
               size = 80.dp
            )
            Column(
               modifier = modifier.width(IntrinsicSize.Min),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center
            ) {
               Text(
                  text = "Expiration",
                  style = MaterialTheme.typography.displaySmall
               )

               Row(
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = modifier.fillMaxWidth()
               ) {
                  "ALERT".forEach {
                     Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                     )
                  }
               }

            }
         }
      }

      Column(
         modifier = modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(top = 15.dp, bottom = 25.dp),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.SpaceBetween
      ) {
         Text(
            modifier = modifier,
            style = MaterialTheme.typography.labelLarge,
            text = "Sign in to continue",
         )

         OutlinedButton(onClick = onAuthenticate) {
            Row(
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
               PainterIcon(
                  iconId = R.drawable.google_logo,
                  contentDescription = "Google logo"
               )
               Text(
                  text = "Sign In With Google"
               )
            }
         }
      }
   }
}

@Preview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      AuthenticationScreenContent(
         onAuthenticate = {}
      )
   }
}