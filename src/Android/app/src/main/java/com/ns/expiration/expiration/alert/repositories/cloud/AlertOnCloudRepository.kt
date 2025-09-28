package com.ns.expiration.expiration.alert.repositories.cloud

import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class AlertOnCloudRepository(
   val firestore: FirebaseFirestore,
   val storage: FirebaseStorage
) {
   fun deleteAlert(alertId: String, reminderIds: List<String>, imageName: String) {

      Firebase.auth.currentUser?.let { user ->
         firestore.runTransaction {
            firestore.runTransaction {
               firestore.collection("users").document(user.uid)
                  .collection("alerts").document(alertId)
                  .delete()
                  .addOnFailureListener {
                     Firebase.crashlytics.recordException(it)
                  }
            }

            reminderIds.forEach { reminderId ->
               firestore.collection("users").document(user.uid)
                  .collection("reminders").document(reminderId)
                  .delete()
                  .addOnFailureListener {
                     Firebase.crashlytics.recordException(it)
                  }
            }

         }.addOnSuccessListener {
            val image = storage.reference.child("${user.uid}/images/$alertId/$imageName")
            image.delete()
               .addOnFailureListener {
                  Firebase.crashlytics.recordException(it)
               }
         }
      }
   }

   fun uploadAlert(alert: HashMap<String, Any>, reminders: List<HashMap<String, Any>>, imageUri: String) {
      Firebase.auth.currentUser?.let { user ->

         val alertId = alert["id"].toString()

         firestore.runTransaction {
            firestore.collection("users").document(user.uid)
               .collection("alerts").document(alertId)
               .set(alert)

            reminders.forEach { reminder ->
               firestore.collection("users").document(user.uid)
                  .collection("reminders").document(reminder["id"].toString())
                  .set(reminder)
            }
         }.addOnSuccessListener {
            val imageName = imageUri.split("/").last()
            val image = storage.reference.child("${user.uid}/images/$alertId/$imageName")

            val file = File(imageUri)
            if (file.exists()) {
               image.putFile(file.toUri())
            }
         }
      }
   }
}