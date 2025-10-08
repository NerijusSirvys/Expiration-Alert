package com.ns.expiration.expiration.alert.persistance.converters

import androidx.room.TypeConverter
import java.time.LocalDate

class ListConverter {
   @TypeConverter
   fun fromList(data: List<LocalDate>?): String? {
      return data?.joinToString(separator = "|") { it.toString() }
   }

   @TypeConverter
   fun toList(data: String): List<LocalDate> {
      if (data.isEmpty()) return listOf()

      return data.split("|").map { LocalDate.parse(it) }
   }
}