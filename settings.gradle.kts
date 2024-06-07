enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "app.cash.zipline")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "app.cash.zipline")
    }
}

rootProject.name = "ziplineprototype"
include(":android")
include(":presenters")
include(":presenters:iosMain")
include(":ios:shared")
