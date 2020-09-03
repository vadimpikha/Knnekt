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
    implementation(Libs.RECYCLER_SELECTION)
}

apply(plugin = "com.google.gms.google-services")