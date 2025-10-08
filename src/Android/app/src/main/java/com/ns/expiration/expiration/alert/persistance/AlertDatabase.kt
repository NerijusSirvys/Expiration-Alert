package com.ns.expiration.expiration.alert.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ns.expiration.expiration.alert.persistance.converters.ListConverter
import com.ns.expiration.expiration.alert.persistance.converters.LocalDateConverter
import com.ns.expiration.expiration.alert.persistance.converters.LocalDateTimeConverter
import com.ns.expiration.expiration.alert.persistance.dao.AlertDao
import com.ns.expiration.expiration.alert.persistance.entities.AlertEntity
import com.ns.expiration.expiration.alert.persistance.entities.ReminderEntity


@Database(
   entities = [AlertEntity::class, ReminderEntity::class],
   version = 1
)
@TypeConverters(
   LocalDateConverter::class,
   LocalDateTimeConverter::class,
   ListConverter::class
)
abstract class AlertDatabase() : RoomDatabase() {
   abstract fun alertDao(): AlertDao
}