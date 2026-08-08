import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(localFile.inputStream())
    }
}

android {
    namespace = "com.example.applicationhome"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.applicationhome"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey = localProperties.getProperty("GEOAPIFY_MAP_API_KEY") ?: ""
        buildConfigField("String", "GEOAPIFY_MAP_API_KEY", "\"$apiKey\"")

        val dbUrl = localProperties.getProperty("REALTIME_DB_URL") ?: ""
        buildConfigField("String", "REALTIME_DB_URL", "\"$dbUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    // AndroidX & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation(libs.androidx.compose.foundation.layout)

    // Icons
    implementation("androidx.compose.material:material-icons-extended")

    // UI Utilities
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Networking (Retrofit)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Local Data & Storage
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Room
    implementation("androidx.room:room-runtime:2.7.0")
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.room.ktx)
    ksp("androidx.room:room-compiler:2.7.0")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    // Dependency Injection (Hilt)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.paging:paging-compose:3.3.0")
    implementation("androidx.room:room-paging:2.7.0")

    implementation("commons-net:commons-net:3.9.0")

    // مكتبة Google Maps الخاصة بـ Jetpack Compose
    implementation("com.google.maps.android:maps-compose:6.12.0")

    // مكتبة OpenStreetMap للأندرويد
    implementation("org.osmdroid:osmdroid-android:6.1.18")

    //طلب إذن الموقع
    implementation("com.google.accompanist:accompanist-permissions:0.37.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
}
configurations.all {
    resolutionStrategy {
        force("com.google.dagger:hilt-android:2.57.1")
        force("com.google.dagger:hilt-compiler:2.57.1")
    }
}