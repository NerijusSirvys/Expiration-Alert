package com.ns.expiration.expiration.alert.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingAlert(modifier: Modifier = Modifier) {
   BasicAlertDialog(
      modifier = modifier,
      onDismissRequest = {},
   ) {
      Card(
         modifier = modifier
            .fillMaxSize(0.15f)
      ) {
         Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
         ) {
            CircularProgressIndicator()
         }
      }
   }
}

@Preview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      LoadingAlert()
   }
}