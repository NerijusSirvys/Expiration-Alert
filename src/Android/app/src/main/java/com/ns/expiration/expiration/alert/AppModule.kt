package com.ns.expiration.expiration.alert

import com.ns.expiration.expiration.alert.data.AlertRepository
import com.ns.expiration.expiration.alert.screens.details.AlertDetailsScreenViewmodel
import com.ns.expiration.expiration.alert.screens.home.HomeScreenViewmodel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
   factoryOf(::AlertRepository)

   viewModelOf(::HomeScreenViewmodel)
   viewModelOf(::AlertDetailsScreenViewmodel)
}