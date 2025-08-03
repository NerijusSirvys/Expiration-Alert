package com.ns.expiration.expiration.alert.screens.home

sealed interface HomeScreenAction {
   data class UpdateSearchTerm(val term: String) : HomeScreenAction
}