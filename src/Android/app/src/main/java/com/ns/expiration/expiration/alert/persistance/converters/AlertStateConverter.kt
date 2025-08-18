package com.ns.expiration.expiration.alert.persistance.converters

import androidx.room.TypeConverter
import com.ns.expiration.expiration.alert.repositories.data.AlertState

class AlertStateConverter {
   @TypeConverter
   fun fromAlertState(data: AlertState): Int {
      return data.ordinal
   }

   @TypeConverter
   fun toAlertState(data: Int): AlertState {
      return AlertState.entries[data]
   }
}

