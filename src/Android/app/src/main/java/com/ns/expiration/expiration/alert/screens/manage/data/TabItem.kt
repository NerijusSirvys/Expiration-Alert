package com.ns.expiration.expiration.alert.screens.manage.data

import androidx.compose.ui.graphics.Color
import com.ns.expiration.expiration.alert.ui.theme.SunRay
import com.ns.expiration.expiration.alert.ui.theme.White

data class TabItem(
   val type: AlertScreenTabs,
   val label: String,
   val selectedColor: Color,
   val unselectedColor: Color
)

val tabs = listOf(
   TabItem(
      type = AlertScreenTabs.BasicInfo,
      label = "Basic Info",
      unselectedColor = White,
      selectedColor = SunRay
   ),
   TabItem(
      type = AlertScreenTabs.Reminders,
      label = "Reminders",
      unselectedColor = White,
      selectedColor = SunRay
   ),
   TabItem(
      type = AlertScreenTabs.Picture,
      label = "Picture",
      unselectedColor = White,
      selectedColor = SunRay
   )
)
