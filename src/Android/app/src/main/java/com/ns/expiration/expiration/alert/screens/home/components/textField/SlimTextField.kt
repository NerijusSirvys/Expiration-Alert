package com.ns.expiration.expiration.alert.screens.home.components.textField

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ns.expiration.expiration.alert.ComponentPreview
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.ui.theme.ExpirationAlertTheme
import com.ns.expiration.expiration.alert.ui.theme.SlateGrey
import com.ns.expiration.expiration.alert.ui.theme.SunRay
import com.ns.expiration.expiration.alert.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlimTextField(
   modifier: Modifier = Modifier,
   placeholder: String? = null,
   leadingIcon: TextFieldIcon? = null,
   trailingIcon: TextFieldIcon? = null,
   onTrailingIconClick: (() -> Unit)? = null,
   keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
   keyboardActions: KeyboardActions = KeyboardActions.Default,
   colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
   value: String,
   onValueChange: (String) -> Unit
) {

   Surface(
      modifier = modifier
         .height(50.dp)
         .fillMaxWidth(),
      border = BorderStroke(width = 2.dp, color = SlateGrey),
      color = Color.Transparent,
      shape = MaterialTheme.shapes.medium
   ) {
      val interactionSource = remember { MutableInteractionSource() }

      val colors = colors.copy(
         textSelectionColors = TextSelectionColors(
            handleColor = SunRay,
            backgroundColor = SunRay
         )
      )

      CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
         BasicTextField(
            value = value,
            cursorBrush = SolidColor(White),
            textStyle = LocalTextStyle.current.copy(color = White),
            modifier = modifier,
            onValueChange = { onValueChange.invoke(it) },
            singleLine = true,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            decorationBox = { inner ->
               OutlinedTextFieldDefaults.DecorationBox(
                  value = value,
                  innerTextField = inner,
                  enabled = true,
                  singleLine = true,
//                  colors = colors,
                  placeholder = {
                     placeholder?.let {
                        Text(
                           text = placeholder,
                           overflow = TextOverflow.Ellipsis,
                           color = White.copy(alpha = 0.2f)
                        )
                     }
                  },
                  visualTransformation = VisualTransformation.None,
                  interactionSource = interactionSource,
                  leadingIcon = {
                     leadingIcon?.let {
                        Icon(
                           painter = it.painter,
                           contentDescription = it.contentDescription,
                           tint = White.copy(alpha = 0.2f)
                        )
                     }
                  },
                  trailingIcon = {
                     trailingIcon?.let {
                        IconButton(onClick = onTrailingIconClick ?: {}, enabled = onTrailingIconClick != null) {
                           Icon(
                              painter = it.painter,
                              contentDescription = it.contentDescription,
                              tint = White.copy(alpha = 0.2f)
                           )
                        }
                     }
                  },
                  container = {
                     OutlinedTextFieldDefaults.Container(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
//                        colors = colors,
                        shape = MaterialTheme.shapes.medium,
                     )
                  }
               )
            }
         )
      }
   }
}

@ComponentPreview
@Composable
private fun Preview() {
   ExpirationAlertTheme {
      Column(
         verticalArrangement = Arrangement.Center,
         modifier = Modifier
            .fillMaxSize()
            .padding(25.dp)
      ) {
         Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
         ) {
            SlimTextField(
               value = "Hello Jimmy",
               onValueChange = {}
            )

            SlimTextField(
               value = "Hello Jimmy how are you, this is just a very long text",
               onValueChange = {}
            )

            SlimTextField(
               value = "",
               placeholder = "Placeholder",
               onValueChange = {}
            )

            SlimTextField(
               value = "Hello Jimmy",
               onValueChange = {},
               leadingIcon = TextFieldIcon(painterResource(R.drawable.ic_search), "Search Icon")
            )

            SlimTextField(
               value = "Hello Jimmy",
               onValueChange = {},
               leadingIcon = TextFieldIcon(painterResource(R.drawable.ic_search), "Search Icon"),
               trailingIcon = TextFieldIcon(painterResource(R.drawable.ic_close), "Search Icon")
            )
         }
      }
   }
}