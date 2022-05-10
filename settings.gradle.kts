pluginManagement {
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
}
rootProject.name = "GeyanLib"
include(":shared")

include(":flutter")
project(":flutter").projectDir = file("${project(":shared").projectDir}${File.pathSeparator}src${File.pathSeparator}flutter")
