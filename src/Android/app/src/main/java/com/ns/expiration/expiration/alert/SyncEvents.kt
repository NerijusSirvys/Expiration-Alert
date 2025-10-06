package com.ns.expiration.expiration.alert

sealed interface SyncEvents {
   data object SyncComplete : SyncEvents
   data object SyncFailed : SyncEvents
}