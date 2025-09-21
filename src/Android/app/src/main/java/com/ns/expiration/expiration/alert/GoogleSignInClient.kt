package com.ns.expiration.expiration.alert

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class GoogleSignInClient(
   private val context: Context
) {
   private val credentialManager = CredentialManager.create(context)

   suspend fun signIn(onSuccess: () -> Unit = {}, onError: (message: String) -> Unit = {}) {
      try {
         val credResponse = getCredentialRequest(getGoogleIdOptions())
         val authResult = handleSignIn(credResponse)
         if (authResult == null || authResult.user == null) {
            onError.invoke("Failed to sign in")
         } else {
            onSuccess.invoke()
         }
      } catch (e: Exception) {
         onError.invoke("Failed to sign in")
      }
   }

   fun signedIn(): Boolean {
      return Firebase.auth.currentUser != null
   }

   private suspend fun handleSignIn(credentialResponse: GetCredentialResponse): AuthResult? {
      val credential = credentialResponse.credential
      if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
         val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
         val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)

         return Firebase.auth.signInWithCredential(authCredential).await()
      } else {
         return null
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