package com.ns.expiration.expiration.alert.persistance.converters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
   @TypeConverter
   fun fromLocalDate(data: LocalDate?): String {
      return data?.toString() ?: ""
   }

   @TypeConverter
   fun toLocalDate(data: String): LocalDate? {
      if (data == "") return null

      return LocalDate.parse(data)
   }
}