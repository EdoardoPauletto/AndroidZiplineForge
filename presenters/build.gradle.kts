import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.zipline)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    applyDefaultHierarchyTemplate()

    js {
        browser()
        binaries.executable()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "presenters"
            isStatic = true
        }
    }

    kotlin.sourceSets.named("jsMain") {
        dependencies {
            implementation(npm("package-name", "version"))//serve?
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.zipline)
            }
        }
        val hostMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.zipline.loader)
                api(libs.okio.core)
            }
        }

        val androidMain by getting {
            dependsOn(hostMain)
            dependencies {
                implementation(libs.okHttp.core)
            }
        }
        val iosMain by getting {
            dependsOn(hostMain)
        }
    }
}

android {
    namespace = "it.uninsubria.prototype.presenters"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
}

zipline {
    //Questa proprietà serve alla parte guest (cioè il server)
    mainFunction.set("it.uninsubria.prototype.main")
}

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().yarnLockAutoReplace = true
}



