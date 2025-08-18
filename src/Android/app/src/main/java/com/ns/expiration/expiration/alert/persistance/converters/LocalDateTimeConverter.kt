package com.ns.expiration.expiration.alert.persistance.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocalDateTimeConverter {
   @TypeConverter
   fun fromLocalDateTime(data: LocalDateTime?): String {
      return data?.toString() ?: ""
   }

   @TypeConverter
   fun toLocalDateTime(data: String): LocalDateTime? {
      if (data == "") return null

      return LocalDateTime.parse(data)
   }
}