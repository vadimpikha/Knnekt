plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)
    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        consumerProguardFiles("consumer-proguard-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    lintOptions {
        disable("InvalidPackage", "MissingTranslation")
        disable("GradleDependency")
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

    // AppCompat
    implementation(Libs.APPCOMPAT)

    // Architecture Components
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    implementation(Libs.LIFECYCLE_VIEW_MODEL_KTX)
    api(Libs.LIFECYCLE_COMMON)
    api(Libs.LIFECYCLE_EXTENSIONS)
    implementation(Libs.ROOM_KTX)
    implementation(Libs.ROOM_RUNTIME)
    kapt(Libs.ROOM_COMPILER)
    testImplementation(Libs.ARCH_TESTING)


    implementation(Libs.CORE_KTX)

    // Kotlin
    implementation(Libs.KOTLIN_STDLIB)

    // Coroutines
    api(Libs.COROUTINES)
    testImplementation(Libs.COROUTINES_TEST)

    // Firebase
    api(Libs.FIREBASE_AUTH)

    // Unit tests
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.HAMCREST)
    testImplementation(Libs.MOCKITO_CORE)
    testImplementation(Libs.MOCKITO_KOTLIN)

    // unit tests livedata
    testImplementation(Libs.ARCH_TESTING)

    //Connectycube
    api(Libs.CONNECTYCUBE_PUSHNOTIFICATIONS)
    api(Libs.CONNECTYCUBE_CHAT)
    api(Libs.CONNECTYCUBE_VIDEOCHAT)
    api(Libs.CONNECTYCUBE_STORAGE)

    api(Libs.PAGING)

    implementation(Libs.TIMBER)

    //Kodein
    implementation(Libs.KODEIN)
}