package com.ns.expiration.expiration.alert

import com.ns.expiration.expiration.alert.repositories.AlertRepository
import com.ns.expiration.expiration.alert.screens.details.AlertDetailsScreenViewmodel
import com.ns.expiration.expiration.alert.screens.home.HomeScreenViewmodel
import com.ns.expiration.expiration.alert.screens.manage.ManageAlertScreenViewmodel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
   factoryOf(::AlertRepository)

   viewModelOf(::HomeScreenViewmodel)
   viewModelOf(::AlertDetailsScreenViewmodel)
   viewModel { (alertId: String) ->
      ManageAlertScreenViewmodel(alertId, get(), get())
   }
}