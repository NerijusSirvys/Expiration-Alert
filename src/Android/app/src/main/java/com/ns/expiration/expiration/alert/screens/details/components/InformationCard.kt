package com.ns.expiration.expiration.alert.screens.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.ui.theme.SlateGrey

@Composable
fun InformationCard(
   modifier: Modifier = Modifier,
   label: String,
   content: @Composable () -> Unit
) {
   Card(
      modifier = modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = SlateGrey)
   ) {
      Column(
         modifier = modifier.padding(vertical = 15.dp, horizontal = 25.dp)
      ) {
         Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
         )
         HorizontalDivider()
         Spacer(modifier = Modifier.height(3.dp))
         content.invoke()
      }
   }
}