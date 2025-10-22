package com.ns.expiration.expiration.alert

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.util.DebugLogger
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import com.ns.expiration.expiration.alert.notifications.NotificationController
import com.ns.expiration.expiration.alert.persistance.roomModule
import com.ns.expiration.expiration.alert.schedulers.AlarmScheduler
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class MyApplication : Application(), SingletonImageLoader.Factory {
   override fun onCreate() {
      super.onCreate()

      startKoin {
         androidLogger()
         androidContext(this@MyApplication)
         workManagerFactory()
         modules(appModule, roomModule)
      }

      Firebase.initialize(this@MyApplication)
      FirebaseFirestore.setLoggingEnabled(true)

      val notificationController by inject<NotificationController>()
      notificationController.createChannel()

      val scheduler by inject<AlarmScheduler>()
      scheduler.setImmediate()

   }

   override fun newImageLoader(context: PlatformContext): ImageLoader {
      return ImageLoader(this).newBuilder()
         .memoryCachePolicy(CachePolicy.ENABLED)
         .memoryCache {
            MemoryCache.Builder()
               .maxSizePercent(this, 0.1)
               .strongReferencesEnabled(true)
               .build()
         }
         .diskCachePolicy(CachePolicy.ENABLED)
         .diskCache {
            DiskCache.Builder()
               .maxSizePercent(0.03)
               .directory(cacheDir)
               .build()
         }
         .logger(DebugLogger())
         .build()
   }
}