package com.ns.expiration.expiration.alert.components.navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.ns.expiration.expiration.alert.ApplicationActions
import com.ns.expiration.expiration.alert.ApplicationState
import com.ns.expiration.expiration.alert.R

@Composable
fun DrawerContent(
   modifier: Modifier = Modifier,
   appState: ApplicationState,
   onAction: (ApplicationActions) -> Unit
) {

   val rotation by if (appState.syncing) rememberInfiniteTransition("spin")
      .animateFloat(
         initialValue = 0f,
         targetValue = -360f,
         animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
         ),
         label = "deg"
      )
   else animateFloatAsState(targetValue = 0f, label = "deg")

   ModalDrawerSheet(
      modifier = modifier,
      drawerContainerColor = MaterialTheme.colorScheme.onPrimary
   ) {
      NavigationDrawerItem(
         label = {
            Text("Sync")
         },
         icon = {
            Icon(
               painter = painterResource(R.drawable.ic_sync),
               contentDescription = "",
               modifier.rotate(rotation)
            )
         },
         selected = false,
         onClick = { onAction(ApplicationActions.Sync) }
      )
   }
}