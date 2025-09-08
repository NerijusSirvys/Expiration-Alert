package com.ns.expiration.expiration.alert.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ns.expiration.expiration.alert.schedulers.AlarmScheduler
import org.koin.java.KoinJavaComponent

class BootCompleteReceived : BroadcastReceiver() {
   override fun onReceive(context: Context, intent: Intent) {
      if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
      val scheduler by KoinJavaComponent.inject<AlarmScheduler>(AlarmScheduler::class.java)
      scheduler.setImmediate()
   }
}