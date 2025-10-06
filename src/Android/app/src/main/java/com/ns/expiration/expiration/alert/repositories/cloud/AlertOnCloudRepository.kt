package com.ns.expiration.expiration.alert.repositories.cloud

import androidx.core.net.toUri
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

class AlertOnCloudRepository(
   val firestore: FirebaseFirestore,
   val storage: FirebaseStorage
) {
   suspend fun downloadAlerts(userId: String): List<Map<String?, Any?>>? {
      return firestore.collection("users").document(userId)
         .collection("alerts")
         .get()
         .await().map { it.data }
   }

   suspend fun downloadReminders(userId: String): List<Map<String?, Any?>> {
      return firestore.collection("users").document(userId)
         .collection("reminders")
         .get().await().map { it.data }
   }

   suspend fun downloadAlertImage(userId: String, imageUri: String, alertId: String) {
      val imageName = imageUri.split("/").last()
      val image = storage.reference.child("${userId}/images/$alertId/$imageName")
      image.getFile(imageUri.toUri()).await()
   }

   fun deleteAlert(userId: String, alertId: String, reminderIds: List<String>, imageName: String) {
      val batch = firestore.batch()
      val doc = firestore.collection("users").document(userId)
      batch.delete(doc.collection("alerts").document(alertId))

      reminderIds.forEach { reminderId ->
         val reminderDoc = firestore.collection("users").document(userId)
         batch.delete(reminderDoc.collection("reminders").document(reminderId))
      }

      batch.commit().addOnSuccessListener {
         val image = storage.reference.child("${userId}/images/$alertId/$imageName")
         image.delete()
            .addOnFailureListener {
               Firebase.crashlytics.recordException(it)
            }
      }
   }

   fun uploadAlert(userId: String, alert: HashMap<String, Any>, reminders: List<HashMap<String, Any>>, imageUri: String) {

      val alertId = alert["id"].toString()

      val userDoc = firestore.collection("users").document(userId)
      firestore.runTransaction { thx ->
         thx.set(userDoc.collection("alerts").document(alertId), alert)

         reminders.forEach { reminder ->
            thx.set(userDoc.collection("reminders").document(reminder["id"].toString()), reminder)
         }

      }.addOnSuccessListener {
         val imageName = imageUri.split("/").last()
         val image = storage.reference.child("${userId}/images/$alertId/$imageName")

         val file = File(imageUri)
         if (file.exists()) {
            image.putFile(file.toUri())
         }
      }
   }
}