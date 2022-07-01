import java.io.FileInputStream
import java.util.*


plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
}

val isHiltEnabled = true

if (isHiltEnabled)
    apply(plugin = "dagger.hilt.android.plugin")

val versionMajor = 0
val versionMinor = 0
val versionPatch = 1
val versionBuild = 0

val composeVersion = "1.2.0-rc01"
val lifecycleVersion = "2.5.0-rc02"
val navigationVersion = "2.5.0-rc02"
val roomVersion = "2.4.2"
val accompanistVersion = "0.24.11-rc"
val kotlinVersion = "1.6.21"
val hiltVersion = "2.42"
val composeMd3Version = "1.0.0-alpha13"
val coilVersion = "2.1.0"
val exoPlayerVersion = "2.18.0"
val retrofitVersion = "2.9.0"
val rssParserVersion = "0.6.0"
val isDebug = false

android {
    compileSdk = 32
    if (isDebug) {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        signingConfigs {
            all {
                keyAlias = keystoreProperties["keyAlias"].toString()
                keyPassword = keystoreProperties["keyPassword"].toString()
                storeFile = file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"].toString()
            }
        }
    }
    defaultConfig {
        applicationId = "io.github.junkfood.podcast"
        minSdk = 26
        targetSdk = 32
        versionCode = versionMajor * 1000 + versionMinor * 100 + versionPatch * 10 + versionBuild
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.0-alpha05")
    implementation("com.google.android.material:material:1.7.0-alpha02")
    implementation("androidx.activity:activity-compose:1.6.0-alpha05")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material3:material3:$composeMd3Version")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.navigation:navigation-compose:$navigationVersion")
    implementation("androidx.compose.animation:animation-graphics:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")

    implementation("io.coil-kt:coil-compose:$coilVersion")

    if (isHiltEnabled) {
        implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
        kapt("androidx.hilt:hilt-compiler:1.0.0")
        implementation("com.google.dagger:hilt-android:$hiltVersion")
        kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    }

    implementation("com.google.android.exoplayer:exoplayer:$exoPlayerVersion")

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("com.tencent:mmkv:1.2.13")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.icosillion.podengine:podengine:2.4.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
}