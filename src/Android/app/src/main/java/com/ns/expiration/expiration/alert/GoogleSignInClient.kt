package com.ns.expiration.expiration.alert

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class GoogleSignInClient(
   private val context: Context
) {
   private val credentialManager = CredentialManager.create(context)

   suspend fun signIn(): Boolean {
      try {
         val result = getCredentialRequest(getGoogleIdOptions())
         return handleSignIn(result)
      } catch (e: Exception) {
         return false
      }
   }

   fun signedIn(): Boolean {
      return Firebase.auth.currentUser != null
   }

   private suspend fun handleSignIn(credentialResponse: GetCredentialResponse): Boolean {

      val credential = credentialResponse.credential

      if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
         val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
         val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)

         val authResult = Firebase.auth.signInWithCredential(authCredential).await()
         return authResult != null
      } else {
         return false
      }
   }

   private suspend fun getCredentialRequest(googleIdOption: GetGoogleIdOption): GetCredentialResponse {
      val request = GetCredentialRequest.Builder()
         .addCredentialOption(googleIdOption)
         .build()

      return credentialManager.getCredential(request = request, context = context)
   }

   private fun getGoogleIdOptions(): GetGoogleIdOption {
      return GetGoogleIdOption.Builder()
         .setServerClientId("868802800835-61lb1od74ss9vf768rhel1u8lrq58rnl.apps.googleusercontent.com")
         .setAutoSelectEnabled(true)
         .setFilterByAuthorizedAccounts(false)
         .build()
   }
}