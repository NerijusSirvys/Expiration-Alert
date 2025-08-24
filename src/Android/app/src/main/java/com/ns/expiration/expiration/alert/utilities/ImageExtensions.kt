package com.ns.expiration.expiration.alert.utilities

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toWebPStream(quality: Int = 100): ByteArray {
   val stream = ByteArrayOutputStream()
   this.compress(Bitmap.CompressFormat.WEBP_LOSSY, quality, stream)
   return stream.toByteArray()
}