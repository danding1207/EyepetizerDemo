plugins {
    id("com.android.library")
    id("kotlin-kapt")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Libs.build_versions_compile_sdk)
    buildToolsVersion(Libs.build_versions_build_tools)

    defaultConfig {
        minSdkVersion(Libs.build_versions_min_sdk)
        targetSdkVersion(Libs.build_versions_target_sdk)
        versionCode = Libs.versionCode
        versionName = Libs.versionName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

kapt {
//    generateStubs = true
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Support libraries
    api(Libs.support_app_compat)
    api(Libs.support_recyclerview)
    api(Libs.support_cardview)
    api(Libs.support_design)
    api(Libs.support_v4)
    api(Libs.support_constraint_layout)

    // kotlin
    api(Libs.kotlin_stdlib)

    // Architecture components lifecycle
    api(Libs.lifecycle_runtime)
    api(Libs.lifecycle_extensions)
    kapt(Libs.lifecycle_compiler)

    // Architecture components room
    api(Libs.room_runtime)
    api(Libs.room_rxjava2)
    kapt(Libs.room_compiler)

    // Architecture components paging
    api(Libs.paging)

    // Architecture components work
    api(Libs.work_runtime)

    // Architecture components navigation
    api(Libs.navigation_runtime)
    api(Libs.navigation_fragment)

    //glide
    api(Libs.glide_runtime)
    kapt(Libs.glide_compiler)

    //retrofit + okhttp
    api(Libs.retrofit_runtime)
    api(Libs.retrofit_gson)
    api(Libs.retrofit_rxjava_adapter)
    api(Libs.okhttp_logging_interceptor)
    api(Libs.okhttp_runtime)
    api(Libs.gson)

    // RxJava
    api(Libs.rxjava)
    api(Libs.rx_android)

    //router
    api(Libs.arouter_runtime)
//    kapt(Libs.arouter_compiler)

    //logger
    api(Libs.logger)
}