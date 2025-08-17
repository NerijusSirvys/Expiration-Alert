package com.ns.expiration.expiration.alert.persistance

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.dsl.module

val roomModule = module {
   single {
      Room.databaseBuilder(
         get(),
         AlertDatabase::class.java,
         "alert_db"
      ).addCallback(object : RoomDatabase.Callback() {
         override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            AlertDatabase.prepopulateDatabase(get())
         }
      }).build()
   }

   single { get<AlertDatabase>().alertDao() }
}