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

   suspend fun deleteAlert(userId: String, alertId: String, reminderIds: List<String>, imageName: String) {
      try {
         firestore.runBatch { batch ->
            val doc = firestore.collection("users").document(userId)
            batch.delete(doc.collection("alerts").document(alertId))

            reminderIds.forEach { reminderId ->
               val reminderDoc = firestore.collection("users").document(userId)
               batch.delete(reminderDoc.collection("reminders").document(reminderId))
            }
         }.await()
      } catch (e: Exception) {
         Firebase.crashlytics.recordException(Exception("Failed to delete Alert With Reminders", e))
      }

      try {
         val image = storage.reference.child("${userId}/images/$alertId/$imageName")
         image.delete().await()
      } catch (e: Exception) {
         Firebase.crashlytics.recordException(Exception("Image deletion failed", e))
      }
   }

   suspend fun uploadAlert(userId: String, alert: HashMap<String, Any>, reminders: List<HashMap<String, Any>>, imageUri: String) {

      val alertId = alert["id"].toString()

      val userDoc = firestore.collection("users").document(userId)

      firestore.runBatch { batch ->
         batch.set(userDoc.collection("alerts").document(alertId), alert)

         reminders.forEach { reminder ->
            batch.set(userDoc.collection("reminders").document(reminder["id"].toString()), reminder)
         }
      }.await()

      val imageName = imageUri.split("/").last()
      val image = storage.reference.child("${userId}/images/$alertId/$imageName")

      val file = File(imageUri)
      if (file.exists()) {
         image.putFile(file.toUri()).await()
      }
   }
}