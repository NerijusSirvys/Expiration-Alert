package com.ns.expiration.expiration.alert.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeHelpers {
   fun convertMillisToDate(millis: Long): String {
      val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
      return formatter.format(Date(millis))
   }
}
