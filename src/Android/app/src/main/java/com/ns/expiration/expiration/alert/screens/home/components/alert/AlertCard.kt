package com.ns.expiration.expiration.alert.screens.home.components.alert

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.ns.expiration.expiration.alert.ComponentPreview
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme

@Composable
fun AlertCard(
   modifier: Modifier = Modifier,
   imageUrl: String,
   name: String,
   quantity: Int,
   expiration: String,
   reminders: Int,
   options: AlertCardOptions = AlertCardOptions.Default,
   onClick: () -> Unit = {}
) {
   Surface(
      color = options.color,
      shape = MaterialTheme.shapes.medium,
      modifier = modifier
         .fillMaxWidth()
         .clickable(onClick = onClick)
         .height(options.height)
   ) {
      Row(
         verticalAlignment = Alignment.CenterVertically,
      ) {

         // Card image section
         SubcomposeAsyncImage(
            modifier = Modifier
               .width(options.imageAreaWidth)
               .fillMaxHeight(),
            model = imageUrl,
            clipToBounds = true,
            contentScale = ContentScale.FillWidth,
            contentDescription = "Card Image",
            loading = { CardImageLoader(color = options.loaderColor) },
            error = {
               CardImageError(text = "No Image")
            }
         )

         Column(
            verticalArrangement = Arrangement.spacedBy(7.dp),
            horizontalAlignment = Alignment.Start,
            modifier = modifier.padding(horizontal = 20.dp)
         ) {
            Text(
               text = name,
               style = MaterialTheme.typography.titleLarge,
               color = options.contentColor,
               overflow = TextOverflow.Ellipsis,
               maxLines = 1
            )

            Row(
               horizontalArrangement = Arrangement.SpaceBetween,
               modifier = modifier.fillMaxWidth()
            ) {
               Text(
                  text = "Exp: $expiration",
                  style = MaterialTheme.typography.labelLarge,
                  color = options.contentColor
               )

               // Card content reminders
               Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(5.dp)
               ) {
                  Icon(
                     painter = painterResource(R.drawable.ic_notifications),
                     contentDescription = "Reminder Icon",
                     tint = options.contentColor,
                     modifier = Modifier.size(18.dp)
                  )
                  Text(
                     text = reminders.toString(),
                     color = options.contentColor
                  )
               }

               // Card content quantity
               Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(5.dp)
               ) {
                  Icon(
                     painter = painterResource(R.drawable.ic_stacks),
                     contentDescription = "Quantity Icon",
                     tint = options.contentColor,
                     modifier = Modifier.size(18.dp)
                  )
                  Text(
                     text = quantity.toString(),
                     color = options.contentColor
                  )
               }
            }
         }
      }
   }
}

@ComponentPreview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      Box(
         modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
         contentAlignment = Alignment.Center
      ) {

         AlertCard(
            name = "Item 1 skdjflsjf  lsjflsjf lksdjf",
            expiration = "12/05/2024",
            reminders = 5,
            quantity = 5,
            imageUrl = ""
         )
      }
   }
}