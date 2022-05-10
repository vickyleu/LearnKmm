buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            setUrl("http://download.flutter.io")
            isAllowInsecureProtocol = true
        }
        maven(url ="https://jitpack.io")
        maven(url ="https://kotlin.bintray.com/kotlinx")

    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.android.tools.build:gradle:7.1.3")
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("http://download.flutter.io")
            isAllowInsecureProtocol = true
        }
        maven(url ="https://jitpack.io")
        maven(url ="https://kotlin.bintray.com/kotlinx")
        maven { setUrl("https://mvn.getui.com/nexus/content/repositories/releases")  }
    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}