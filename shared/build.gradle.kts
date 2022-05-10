plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

//project level
val library_version = "1.0.0"

//app level
val gradle = "7.1.3"
val kotlin = "1.6.21"


val deploymentTarget = "10.0"
val compileSdkValue = 32
val minSdkValue = 21
val targetSdkValue = 29

//libs
val flutter_lib = "1.0.0-029e8fdf54966a866004d9f4b937aa6d724a4d4d"
val coreKtx = "1.2.0"
val koin = "3.2.0-beta-1"
val kodein_di = "7.11.0"

val gysdk = "3.0.3.0"
val gysdk_gtc = "3.1.7.0"
val appcompat = "1.3.0-alpha01"
val constraintLayout = "2.0.1"
val material = "1.2.1"
val navigation = "2.3.0"
val firebase_auth_android = "19.4.0"
val coroutines = "1.6.1"
val coroutines_native = "1.3.8"
val viewmodel = "2.3.0-alpha07"

//test
val junit = "4.12"
val extJunit = "1.1.1"
val espresso = "3.2.0"


version = library_version

kotlin {
    android()

    listOf(
//        iosX64(),
        iosArm64(),
//        iosSimulatorArm64()
    ).forEach {
        tasks.register<org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask>("releaseIOSFramework") {
            baseName = "shared"
            destinationDir = buildDir.resolve("cocoapods/framework")
            val isReleaseBuild = !"$version".contains("dev", ignoreCase = false)
            val buildType = if (isReleaseBuild) "RELEASE" else "DEBUG"
//            from(
//                it.binaries.getFramework(buildType),
//            )
        }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            // Enable concurrent sweep phase in new native memory manager. (This will be enabled by default in 1.7.0)
            // https://kotlinlang.org/docs/whatsnew1620.html#concurrent-implementation-for-the-sweep-phase-in-new-memory-manager
//            freeCompilerArgs = freeCompilerArgs + "-Xgc=cms"
        }
        compilations["main"].kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
    }

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.CInteropProcess::class.java) {
        settings.extraOpts(listOf("-compiler-option", "-DNS_FORMAT_ARGUMENT(A)="))
    }


    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = deploymentTarget

        specRepos {
            url("https://github.com/CocoaPods/Specs.git")
            url("https://github.com/bytedance/cocoapods_sdk_source_repo.git")
            url("https://github.com/CocoaPods/Specs")
            url("https://mirrors.tuna.tsinghua.edu.cn/git/CocoaPods/Specs.git")
            url("https://cdn.cocoapods.org/")
        }
        pod("Flutter")
        framework {
            baseName = "shared"
            isStatic = false
            // Bitcode embedding
            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.Framework.BitcodeEmbeddingMode.BITCODE)
        }
//       pod("GYSDK"){
//            //BUG,因为Pod名字与实际library名字不一样,目前需要找到真实的Framework名字才能使用
//            //类似于 https://github.com/CocoaPods/Specs/XXXXXXXX/GYSDK/2.2.0.0/GYSDK.podspec.json
//            version = gysdk_pod
//            moduleName = "GeYanSdk"
//       }
        pod("GYSDK") {
            moduleName = "GeYanSdk" //BUG,因为Pod名字与实际library名字不一样,目前需要找到真实的Framework名字才能使用
            source = path(project.file("../libs/getui-gysdk-ios-cocoapods-2.2.0.0"))
        }
        pod("GTCommonSDK") {
            source = path(project.file("../libs/getui-gtcsdk-ios-cocoapods-1.2.8.0-spm"))
        }

        useLibraries()
        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE
    }
    sourceSets {

        all {
            languageSettings.apply {
                languageVersion = "1.7" // possible values: '1.0', '1.1', '1.2', '1.3', '1.4', '1.5', '1.6', '1.7', '1.8', '1.9'
                apiVersion = "1.7" // possible values: '1.0', '1.1', '1.2', '1.3', '1.4', '1.5', '1.6', '1.7'
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                enableLanguageFeature("InlineClasses") // language feature name
                optIn("kotlin.ExperimentalUnsignedTypes") // annotation FQ-name
                progressiveMode = true // false by default
            }
        }
        val commonMain by getting{
            dependencies {
                // Koin for Kotlin apps
//                implementation("io.insert-koin:koin-core:${koin}")
                implementation("org.kodein.di:kodein-di:${kodein_di}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines}")
            }
        }

        val androidMain by getting{
            dependsOn(commonMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${coroutines}")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${viewmodel}")
                //个验sdk
                implementation("com.getui:gysdk:${gysdk}")
                //个推公共库，如已接其他个推sdk则保留一个最高版本即可
                implementation("com.getui:gtc:${gysdk_gtc}")
//                implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                compileOnly("io.flutter:flutter_embedding_debug:$flutter_lib")
                compileOnly("io.flutter:arm64_v8a_debug:$flutter_lib")
                compileOnly("io.flutter:armeabi_v7a_debug:$flutter_lib")
                compileOnly("io.flutter:x86_64_debug:$flutter_lib")
                compileOnly("io.flutter:x86_debug:$flutter_lib")
            }
        }

//        val iosX64Main by getting
        val iosArm64Main by getting
//        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            val sourceList = arrayListOf<Any>()
            sourceList.addAll(resources.srcDirs)
            sourceList.add(file("${projectDir.parentFile}${File.separator}libs"))
            resources.setSrcDirs(sourceList)
//            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
//            iosSimulatorArm64Main.dependsOn(this)
        }
        configure(listOf(iosArm64Main,)) {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${coroutines_native}")
            }
        }




    }

}

android {
    compileSdk = compileSdkValue
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = minSdkValue
        targetSdk = targetSdkValue
        manifestPlaceholders["GETUI_APPID"] = "你的  GETUI_APPID"
        manifestPlaceholders["GT_INSTALL_CHANNEL"] = "你的 GT_INSTALL_CHANNEL"
    }
    compileOptions {
//        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.named<org.jetbrains.kotlin.gradle.tasks.DefFileTask>("generateDefFlutter").configure {
    doLast {
        println("generateDefFlutter start")
        val includeDir = File(projectDir, "build/cocoapods/synthetic/IOS/shared/Pods/Flutter/Flutter.xcframework/ios-arm64_armv7/Flutter.framework/Headers")
        val headers = listOf("${includeDir.path}/Flutter.h")
        headers.forEach {
            println("generateDefFlutter hearder:$it")
        }
        outputFile.writeText("""
            language = Objective-C
            headers = ${headers.joinToString(" ")}
             """
        )
    }
}

/*
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)*/
