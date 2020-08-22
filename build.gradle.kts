buildscript {

    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url = uri("https://github.com/ConnectyCube/connectycube-android-sdk-releases/raw/master/") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.0-rc01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("com.google.gms:google-services:${Versions.GOOGLE_SERVICES}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()

        maven { url = uri("https://github.com/ConnectyCube/connectycube-android-sdk-releases/raw/master/") }
    }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs +=
            "-Xuse-experimental=" +
                    "kotlin.Experimental," +
                    "kotlinx.coroutines.ExperimentalCoroutinesApi," +
                    "kotlinx.coroutines.InternalCoroutinesApi," +
                    "kotlinx.coroutines.FlowPreview"
    }
}

/*
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
 */