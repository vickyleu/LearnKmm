import com.android.build.gradle.internal.tasks.factory.registerTask

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

//project level
val library_version = "1.0.0"

//app level
val kotlin = "1.6.21"


val deploymentTarget = "10.0"
val compileSdkValue = 32
val minSdkValue = 21
val targetSdkValue = 29

//libs
val flutter_lib = "1.0.0-d1b9a6938ad77326ac3a94d92bbc77933ed829ed"
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

    /*targets{
        fromPreset(presets.linuxX64, 'linuxX64')
        fromPreset(presets.iosArm64, 'iosArm64')
        fromPreset(presets.iosArm32, 'iosArm32')
        fromPreset(presets.iosX64, 'iosX64')
        fromPreset(presets.macosX64, 'macosX64')
        fromPreset(presets.mingwX64, 'windowsX64')
    }*/
    android()

    val iosList = listOf(
        iosArm32(),
        iosArm64(),
    )
    iosList.forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    // Create a task to build a fat framework.
    tasks.register<org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask>("releaseFatFramework") {
        // The fat framework must have the same base name as the initial frameworks.
        baseName = "shared"
        // The default destination directory is "<build directory>/fat-framework".
        destinationDir = buildDir.resolve("fat-framework/release")
        // Specify the frameworks to be merged.
        from(iosList.map{it.binaries.getFramework("RELEASE")}).also {
            val dir = File("${projectDir.parentFile}/flutter/ios/Classes/")
            val framework = File(dir,"$baseName.framework")
            val fatFramework = File(destinationDir,"$baseName.framework")
            if(framework.exists()){
                framework.delete()
            }
            if(fatFramework.exists()){
                fatFramework.copyRecursively(File(dir,"$baseName.framework"),overwrite = true)
            }
        }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            // Enable concurrent sweep phase in new native memory manager. (This will be enabled by default in 1.7.0)
//            freeCompilerArgs = freeCompilerArgs + "-Xgc=cms"
        }
        compilations["main"].kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
    }
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.CInteropProcess::class.java) {
        settings.extraOpts(listOf("-compiler-option", "-DNS_FORMAT_ARGUMENT(A)="))
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = deploymentTarget
        version = "1.0.0"
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
                languageVersion = "1.6" // possible values: '1.0', '1.1', '1.2', '1.3', '1.4', '1.5', '1.6', '1.7', '1.8', '1.9'
                apiVersion = "1.6" // possible values: '1.0', '1.1', '1.2', '1.3', '1.4', '1.5', '1.6', '1.7'
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                enableLanguageFeature("InlineClasses") // language feature name
                optIn("kotlin.ExperimentalUnsignedTypes") // annotation FQ-name
                progressiveMode = true // false by default
            }
        }
        val commonMain by getting{
            dependencies {
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

        val iosArm32Main by getting {
            kotlin.srcDir("src/iosMain")
            dependsOn(commonMain)
            val sourceList = arrayListOf<Any>()
            sourceList.addAll(resources.srcDirs)
            sourceList.add(file("${projectDir.parentFile}/libs"))
            resources.setSrcDirs(sourceList)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${coroutines_native}")
            }
        }

        val iosArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            val sourceList = arrayListOf<Any>()
            sourceList.addAll(resources.srcDirs)
            sourceList.add(file("${projectDir.parentFile}/libs"))
            resources.setSrcDirs(sourceList)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${coroutines_native}")
            }
        }
    }

}

android {
    compileSdk = compileSdkValue

    sourceSets {
        getByName("main") {
            manifest.srcFile ("src/androidMain/AndroidManifest.xml")
        }
    }
    defaultConfig {
        minSdk = minSdkValue
        targetSdk = targetSdkValue
        manifestPlaceholders["GETUI_APPID"] = "你的  GETUI_APPID"
        manifestPlaceholders["GT_INSTALL_CHANNEL"] = "你的 GT_INSTALL_CHANNEL"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lintOptions {
        isAbortOnError = false
    }
//    lint{
//        abortOnError = false
//    }
    repositories{
        all {
            if(this is MavenArtifactRepository){
                if(url.toString().startsWith("https://kotlin.bintray.com/kotlinx",ignoreCase = true)){
                    remove(this)
                }
            }
        }
    }
}

afterEvaluate {
    val releaseAARCopy = tasks.registerTask(taskName = "releaseAARCopy",taskType = Copy::class.java,action = object :
        com.android.build.gradle.internal.tasks.factory.TaskConfigAction<Copy>{
        override fun configure(task: Copy) {
            val fromPath = arrayListOf(
                buildDir.absolutePath,
                "outputs",
                "aar"
            ).joinToString(separator = File.separator)
            val destPath = arrayListOf(
                projectDir.parentFile.absolutePath,
                "flutter",
                "android",
                "libs"
            ).joinToString(separator = File.separator)
            task.from(fromPath)
            task.include("${project.name}-release.aar")
            task.into(destPath)
            task.rename("${project.name}-release.aar","${project.name}.aar")
//            println("Android AAR文件已经拷贝到${destPath}")

        }
    })

    tasks.named("assemble") { finalizedBy(releaseAARCopy) }

    val releaseFrameworkCopy = tasks.registerTask(taskName = "releaseFrameworkCopy",taskType = Copy::class.java,action = object :
        com.android.build.gradle.internal.tasks.factory.TaskConfigAction<Copy>{
        override fun configure(task: Copy) {
            val fromPath = arrayListOf(
                buildDir.absolutePath,
                "fat-framework",
                "release"
            ).joinToString(separator = File.separator)
            val destPath = arrayListOf(
                projectDir.parentFile.absolutePath,
                "flutter",
                "ios",
                "Classes"
            ).joinToString(separator = File.separator)
            task.from(fromPath)
            task.include("${project.name}.framework")
            task.into(destPath)

        }
    })
    tasks.named("assemble") { finalizedBy(releaseFrameworkCopy) }
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
