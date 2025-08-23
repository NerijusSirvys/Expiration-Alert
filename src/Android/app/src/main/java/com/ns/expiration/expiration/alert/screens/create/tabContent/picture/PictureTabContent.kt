package com.ns.expiration.expiration.alert.screens.create.tabContent.picture

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import com.ns.expiration.expiration.alert.R
import com.ns.expiration.expiration.alert.components.PainterIcon
import com.ns.expiration.expiration.alert.components.buttons.PrimaryButton
import com.ns.expiration.expiration.alert.ui.theme.White

@Composable
fun PictureTabContent(
   modifier: Modifier = Modifier,
   image: Bitmap?,
   onPhotoTaken: (Bitmap) -> Unit,
   onResetPhoto: () -> Unit
) {
   var startPreview by remember { mutableStateOf(false) }
   val context = LocalContext.current
   val cameraController = remember {
      LifecycleCameraController(context).apply {
         setEnabledUseCases(CameraController.IMAGE_CAPTURE)
      }
   }

   Column(
      modifier = modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Box(
         modifier = modifier.weight(1f)
      ) {

         if (image != null) {
            AsyncImage(
               modifier = modifier.fillMaxSize(),
               model = image,
               contentDescription = "",
               contentScale = ContentScale.Crop
            )
         } else {
            if (startPreview) {
               CameraPreview(
                  controller = cameraController,
                  modifier = modifier.fillMaxSize()
               )
            } else {
               PainterIcon(
                  iconId = R.drawable.ic_add_camera,
                  contentDescription = "Camera Icon",
                  tint = White.copy(alpha = 0.05f),
                  modifier = modifier
                     .fillMaxSize()
                     .padding(80.dp)
               )
            }
         }
      }

      PrimaryButton(
         text = if (startPreview) "Take Image" else "Start Preview",
         onClick = {
            if (!startPreview) {
               startPreview = true
               onResetPhoto.invoke()
            } else {
               takePhoto(cameraController, context, onPhotoTaken = {
                  onPhotoTaken.invoke(it)
                  startPreview = false
               })
            }
         }
      )
   }
}

private fun takePhoto(controller: LifecycleCameraController, context: Context, onPhotoTaken: (Bitmap) -> Unit) {
   controller.takePicture(
      ContextCompat.getMainExecutor(context),
      object : ImageCapture.OnImageCapturedCallback() {
         override fun onCaptureSuccess(image: ImageProxy) {
            super.onCaptureSuccess(image)
            val rotation = image.imageInfo.rotationDegrees
            val original = image.toBitmap()
            val rotated = if (rotation != 0) {
               val m = android.graphics.Matrix().apply { postRotate(rotation.toFloat()) }
               Bitmap.createBitmap(original, 0, 0, original.width, original.height, m, true)
            } else original

            onPhotoTaken(rotated)
         }

         override fun onError(exception: ImageCaptureException) {
            super.onError(exception)
            Log.e("Camera", "Could not take photo", exception)
         }
      }
   )
}
