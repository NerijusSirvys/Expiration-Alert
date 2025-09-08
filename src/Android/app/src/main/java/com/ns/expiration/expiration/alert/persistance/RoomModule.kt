package com.ns.expiration.expiration.alert.persistance

import androidx.room.Room
import org.koin.dsl.module

val roomModule = module {
   single {
      Room.databaseBuilder(
         get(),
         AlertDatabase::class.java,
         "alert_db"
      ).build()
   }

   single { get<AlertDatabase>().alertDao() }
}