package com.ns.expiration.expiration.alert.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.ns.expiration.expiration.alert.ui.theme.SlateGrey

@Composable
fun AppTextField(
   modifier: Modifier = Modifier,
   value: String,
   onValueChange: (String) -> Unit,
   enabled: Boolean = true,
   readOnly: Boolean = false,
   textStyle: TextStyle = LocalTextStyle.current,
   label: @Composable (() -> Unit)? = null,
   placeholder: @Composable (() -> Unit)? = null,
   leadingIcon: @Composable (() -> Unit)? = null,
   trailingIcon: @Composable (() -> Unit)? = null,
   prefix: @Composable (() -> Unit)? = null,
   suffix: @Composable (() -> Unit)? = null,
   supportingText: @Composable (() -> Unit)? = null,
   isError: Boolean = false,
   visualTransformation: VisualTransformation = VisualTransformation.None,
   keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
   keyboardActions: KeyboardActions = KeyboardActions.Default,
   singleLine: Boolean = false,
   maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
   minLines: Int = 1,
   interactionSource: MutableInteractionSource? = null,
) {
   OutlinedTextField(
      modifier = modifier.fillMaxWidth(),
      value = value,
      label = label,
      readOnly = readOnly,
      enabled = enabled,
      textStyle = textStyle,
      trailingIcon = trailingIcon,
      prefix = prefix,
      suffix = suffix,
      supportingText = supportingText,
      isError = isError,
      visualTransformation = visualTransformation,
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      maxLines = maxLines,
      minLines = minLines,
      interactionSource = interactionSource,

      onValueChange = { onValueChange.invoke(it) },
      shape = MaterialTheme.shapes.medium,
      placeholder = placeholder,
      leadingIcon = leadingIcon,
      colors = OutlinedTextFieldDefaults.colors(
         focusedBorderColor = SlateGrey,
         unfocusedBorderColor = SlateGrey,
         focusedPlaceholderColor = SlateGrey,
         unfocusedPlaceholderColor = SlateGrey,
         focusedLabelColor = SlateGrey,
         unfocusedLabelColor = SlateGrey
      )
   )
}