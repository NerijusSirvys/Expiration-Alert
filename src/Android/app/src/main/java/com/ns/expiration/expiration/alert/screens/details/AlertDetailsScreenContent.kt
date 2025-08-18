package com.ns.expiration.expiration.alert.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.TopBar
import com.ns.expiration.expiration.alert.screens.details.components.InformationCard
import com.ns.expiration.expiration.alert.ui.theme.RedSalsa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDetailsScreenContent(
   modifier: Modifier = Modifier,
   state: AlertDetailsScreenState,
   onAction: (AlertDetailsScreenActions) -> Unit,
   onNavigateBack: () -> Unit = {},
) {
   Scaffold(
      modifier = modifier,
      topBar = {
         TopBar(
            modifier = modifier,
            label = "Alert Details",
            navigationIcon = painterResource(R.drawable.ic_back_arrow),
            onNavigation = onNavigateBack,
         )
      }
   ) { innerPadding ->
      Column(
         verticalArrangement = Arrangement.spacedBy(10.dp),
         modifier = modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
      ) {
         InformationCard(modifier = modifier, label = "Name") {
            Text(
               text = state.data.name,
               overflow = TextOverflow.Ellipsis
            )
         }

         InformationCard(modifier = modifier, label = "Quantity") {
            Text(
               text = state.data.quantity.toString(),
               overflow = TextOverflow.Ellipsis
            )
         }

         InformationCard(modifier = modifier, label = "Expiration") {
            Text(
               text = state.data.expirationDate,
               overflow = TextOverflow.Ellipsis
            )
         }

         InformationCard(modifier = modifier, label = "Notes") {
            Text(
               text = state.data.notes,
               overflow = TextOverflow.Ellipsis
            )
         }

         InformationCard(modifier = modifier, label = "Reminders") {
            state.data.reminders.let {
               it.forEachIndexed { index, item ->
                  Text(
                     text = item,
                     overflow = TextOverflow.Ellipsis
                  )
               }
            }
         }

         InformationCard(modifier = modifier, label = "Image") {
            AsyncImage(
               model = state.data.imageUrl,
               contentDescription = null,
               contentScale = ContentScale.FillWidth,
            )
         }

         OutlinedButton(
            onClick = { onAction.invoke(AlertDetailsScreenActions.DeleteAlert) },
            modifier = modifier.fillMaxWidth(),
            border = ButtonDefaults.outlinedButtonBorder().copy(
               brush = SolidColor(RedSalsa)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
               contentColor = RedSalsa
            )
         ) {
            Text(text = "Delete")
         }
      }
   }
}