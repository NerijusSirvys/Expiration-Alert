package com.ns.expiration.expiration.alert

import android.app.Application
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
import com.ns.expiration.expiration.alert.schedulers.CloudWorker
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import java.time.Duration
import java.util.concurrent.TimeUnit

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

      val constraints = androidx.work.Constraints.Builder()
         .setRequiresCharging(false)
         .setRequiredNetworkType(NetworkType.CONNECTED)
         .build()

      val workRequest = PeriodicWorkRequestBuilder<CloudWorker>(3, TimeUnit.HOURS)
         .setConstraints(constraints)
         .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(15))
         .build()

      val workManager = WorkManager.getInstance(this@MyApplication)
      workManager.enqueueUniquePeriodicWork("cloud-sync", ExistingPeriodicWorkPolicy.KEEP, workRequest)
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