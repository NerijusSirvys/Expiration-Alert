plugins {
   alias(libs.plugins.android.application)
   alias(libs.plugins.kotlin.android)
   alias(libs.plugins.kotlin.compose)
   alias(libs.plugins.serialization)
   alias(libs.plugins.ksp)
}

android {
   namespace = "com.ns.expiration.expiration.alert"
   compileSdk = 36

   defaultConfig {
      applicationId = "com.ns.expiration.expiration.alert"
      minSdk = 30
      targetSdk = 36
      versionCode = 1
      versionName = "1.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
   }

   buildTypes {
      release {
         isMinifyEnabled = false
         proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      }
   }
   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11
   }
   kotlinOptions {
      jvmTarget = "11"
   }
   buildFeatures {
      compose = true
   }

   configurations.configureEach {
      exclude(group = "com.intellij", module = "annotations")
   }
}

dependencies {
   //Camerax
   implementation(libs.camerax.core)
   implementation(libs.camerax.lifecycle)
   implementation(libs.camerax.view)
   implementation(libs.camerax.camera2)
   implementation(libs.camerax.extensions)

   // Image Loading
   implementation(libs.coil.compose)
   implementation(libs.coil.network)

   // Serialization
   implementation(libs.kotlinx.serialization.json)

   // Injection
   implementation(libs.koin.core)
   implementation(libs.koin.android)
   implementation(libs.koin.compose)

   // Room DB
   implementation(libs.room.runtime)
   implementation(libs.room.compiler)
   implementation(libs.room.ktx)

   // Work Manager
   implementation(libs.androidx.work.manager)

   ksp(libs.room.compiler)

   implementation(libs.androidx.navigation.compose)
   implementation(libs.androidx.core.ktx)
   implementation(libs.androidx.lifecycle.runtime.ktx)
   implementation(libs.androidx.activity.compose)
   implementation(platform(libs.androidx.compose.bom))
   implementation(libs.androidx.ui)
   implementation(libs.androidx.ui.graphics)
   implementation(libs.androidx.ui.tooling.preview)
   implementation(libs.androidx.material3)
   androidTestImplementation(platform(libs.androidx.compose.bom))
   debugImplementation(libs.androidx.ui.tooling)
   debugImplementation(libs.androidx.ui.test.manifest)
}