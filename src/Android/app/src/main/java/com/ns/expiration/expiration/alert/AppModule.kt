package com.ns.expiration.expiration.alert

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ns.expiration.expiration.alert.notifications.NotificationController
import com.ns.expiration.expiration.alert.repositories.AlertRepository
import com.ns.expiration.expiration.alert.schedulers.AlarmScheduler
import com.ns.expiration.expiration.alert.screens.details.AlertDetailsScreenViewmodel
import com.ns.expiration.expiration.alert.screens.home.HomeScreenViewmodel
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreenViewmodel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
   factoryOf(::AlertRepository)

   singleOf(::AlarmScheduler)
   singleOf(::NotificationController)
   singleOf(::GoogleSignInClient)
   single {
      FirebaseFirestore.getInstance("app-db")
   }

   single {
      FirebaseStorage.getInstance("gs://expiration-alert-d2ccd.firebasestorage.app")
   }


   viewModelOf(::HomeScreenViewmodel)
   viewModelOf(::AlertDetailsScreenViewmodel)
   viewModel { (alertId: String) ->
      ManageAlertScreenViewmodel(alertId, get(), get())
   }
}