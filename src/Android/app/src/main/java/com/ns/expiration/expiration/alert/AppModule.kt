package com.ns.expiration.expiration.alert

import androidx.work.WorkManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ns.expiration.expiration.alert.notifications.NotificationController
import com.ns.expiration.expiration.alert.repositories.cloud.AlertOnCloudRepository
import com.ns.expiration.expiration.alert.repositories.local.AlertOnDiskRepository
import com.ns.expiration.expiration.alert.schedulers.AlarmScheduler
import com.ns.expiration.expiration.alert.schedulers.CloudWorker
import com.ns.expiration.expiration.alert.screens.details.AlertDetailsScreenViewmodel
import com.ns.expiration.expiration.alert.screens.home.HomeScreenViewmodel
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreenViewmodel
import com.ns.expiration.expiration.alert.services.AlertService
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
   factoryOf(::AlertOnDiskRepository)
   factoryOf(::AlertOnCloudRepository)
   factoryOf(::AlertService)

   singleOf(::AlarmScheduler)
   singleOf(::NotificationController)
   singleOf(::GoogleSignInClient)
   single {
      FirebaseFirestore.getInstance("app-db")
   }

   single {
      FirebaseStorage.getInstance("gs://expiration-alert-d2ccd.firebasestorage.app")
   }

   single {
      WorkManager.getInstance(get())
   }

   workerOf(::CloudWorker)

   viewModelOf(::HomeScreenViewmodel)
   viewModelOf(::AlertDetailsScreenViewmodel)
   viewModel { (alertId: String) ->
      ManageAlertScreenViewmodel(alertId, get(), get())
   }
}