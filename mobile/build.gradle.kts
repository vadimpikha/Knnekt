/*
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'com.google.gms.google-services'
apply plugin: "androidx.navigation.safeargs.kotlin"

ext.kotlin_version = '1.4.0'
ext.connectycube_version = '1.9.2'
ext.lifecycle_version = "2.2.0"
ext.room_version = "2.2.5"
ext.nav_version = '2.3.0'
ext.arch_version = "2.1.0"
ext.kodein_version = "6.5.5"
ext.coroutines_version = "1.3.8"
ext.glide_version = "4.11.0"
ext.arrow_version = "0.10.5"
ext.room_version = "2.2.5"
ext.paging_version = "3.0.0-alpha04"

android {
    compileSdkVersion 30
    buildToolsVersion "30.0.1"

    defaultConfig {
        applicationId "ua.vadimpikha.knnekt"
        minSdkVersion 21
        targetSdkVersion 30
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        buildTypes.each {
            it.resValue 'string', 'connectycube_app_id', connectycube_app_id
            it.resValue 'string', 'connectycube_auth_key', connectycube_auth_key
            it.resValue 'string', 'connectycube_auth_secret', connectycube_auth_secret
            it.resValue 'string', 'connectycube_account_key', connectycube_account_key
            it.resValue 'string', 'firebase_project_id', firebase_project_id
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = '1.8'
        freeCompilerArgs += '-Xopt-in=kotlin.RequiresOptIn'
    }

    buildFeatures {
        dataBinding = true
    }

    androidExtensions {
        experimental = true
    }
}
*/

/*dependencies {

    implementation project(':shared')

    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    testImplementation 'junit:junit:4.13'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

    implementation "org.jetbrains.kotlin:kotlin-stdlib:1.4.0"
    implementation 'androidx.core:core-ktx:1.3.1'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'

    implementation "com.connectycube:connectycube-android-sdk-chat:$connectycube_version"
    implementation "com.connectycube:connectycube-android-sdk-videochat:$connectycube_version"
    implementation "com.connectycube:connectycube-android-sdk-storage:$connectycube_version"
    implementation "com.connectycube:connectycube-android-sdk-pushnotifications:$connectycube_version"

    implementation 'com.google.firebase:firebase-auth:19.3.2'

    implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-service:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-process:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"

    implementation "org.kodein.di:kodein-di-generic-jvm:$kodein_version"
    implementation "org.kodein.di:kodein-di-framework-android-x:$kodein_version"

    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutines_version"

    implementation "com.github.bumptech.glide:glide:$glide_version"

    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    implementation "io.arrow-kt:arrow-core-data:$arrow_version"

    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"

    implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    implementation "androidx.paging:paging-runtime:$paging_version"

    implementation 'com.google.firebase:firebase-messaging:20.2.4'
}*/

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)
    defaultConfig {
        applicationId = "ua.vadimpikha.knnekt"
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "connectycube_app_id", properties["connectycube_app_id"] as String)
        resValue("string", "connectycube_auth_key", properties["connectycube_auth_key"] as String)
        resValue("string", "connectycube_auth_secret", properties["connectycube_auth_secret"] as String)
        resValue("string", "connectycube_account_key", properties["connectycube_account_key"] as String)
        resValue("string", "firebase_project_id", properties["firebase_project_id"] as String)

        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            versionNameSuffix = "-debug"
        }
    }

    buildFeatures {
        dataBinding = true
    }

    androidExtensions {
        isExperimental = true
    }

    lintOptions {
        isCheckGeneratedSources = true
        isCheckReleaseBuilds = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
        options.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {

    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))

    implementation(project(":shared"))

    implementation(Libs.CORE_KTX)

    // UI
    implementation(Libs.ACTIVITY_KTX)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.FRAGMENT_KTX)
    implementation(Libs.CARDVIEW)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.MATERIAL)
    implementation(Libs.FLEXBOX)
    implementation(Libs.LOTTIE)
    implementation(Libs.INK_PAGE_INDICATOR)

    // Architecture Components
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    kapt(Libs.LIFECYCLE_COMPILER)
    testImplementation(Libs.ARCH_TESTING)
    implementation(Libs.NAVIGATION_FRAGMENT_KTX)
    implementation(Libs.NAVIGATION_UI_KTX)
    implementation(Libs.ROOM_KTX)
    implementation(Libs.ROOM_RUNTIME)
    kapt(Libs.ROOM_COMPILER)
    testImplementation(Libs.ROOM_KTX)
    testImplementation(Libs.ROOM_RUNTIME)

    // Glide
    implementation(Libs.GLIDE)
    kapt(Libs.GLIDE_COMPILER)

    // Kotlin
    implementation(Libs.KOTLIN_STDLIB)

    implementation(Libs.SWIPE_REFRESH_LAYOUT)

    implementation(Libs.COROUTINES_ANDROID)

    // Instrumentation tests
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.ESPRESSO_CONTRIB)
    androidTestImplementation(Libs.EXT_JUNIT)
    androidTestImplementation(Libs.RUNNER)
    androidTestImplementation(Libs.RULES)

    // Local unit tests
    testImplementation(Libs.JUNIT)

    implementation(Libs.GSON)

    //Kodein
    implementation(Libs.KODEIN_ANDROID)
    implementation(Libs.KODEIN)


    implementation(Libs.TIMBER)
}

apply(plugin = "com.google.gms.google-services")