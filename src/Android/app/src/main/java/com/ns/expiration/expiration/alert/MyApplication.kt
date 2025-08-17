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
import com.ns.expiration.expiration.alert.persistance.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application(), SingletonImageLoader.Factory {
   override fun onCreate() {
      super.onCreate()

      startKoin {
         androidLogger()
         androidContext(this@MyApplication)
         modules(appModule, roomModule)
      }
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