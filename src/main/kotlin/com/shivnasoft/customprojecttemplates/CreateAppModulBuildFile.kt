package com.shivnasoft.customprojecttemplates

import com.android.tools.idea.wizard.template.ModuleTemplateData
import java.io.File

fun createAppModuleBuildFile(moduleData: ModuleTemplateData) {
    val namespace = moduleData.packageName

    val buildFileContent = """
        plugins {
            id("com.android.application")
            id("org.jetbrains.kotlin.android")
            id("kotlin-parcelize")
            id("dagger.hilt.android.plugin")
            id("kotlin-kapt")
        }

        android {
            namespace = "$namespace"
            compileSdk = Versions.compileSdkVersion

            defaultConfig {
                applicationId = "$namespace"
                minSdk = Versions.minSdkVersion
                targetSdk = Versions.targetSdkVersion
                versionCode = Versions.versionCode
                versionName = Versions.versionName

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                vectorDrawables {
                    useSupportLibrary = true
                }
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            kotlinOptions {
                jvmTarget = "17"
            }

            buildFeatures {
                compose = true
            }

            composeOptions {
                kotlinCompilerExtensionVersion = UI.Versions.composeCompiler
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }

        dependencies {
            implementation(project(":core:common"))
            implementation(project(":core:ui"))
            implementation(project(":core:util"))

            // UI - Activity
            implementation(UI.Activity.activity_activity)
            implementation(UI.Activity.androidx_appcompat)

            // UI - Compose
            implementation(platform(UI.Compose.bom))
            implementation(UI.Compose.ui)
            implementation(UI.Compose.ui_tooling_preview)
            implementation(UI.Compose.runtime_livedata)
            implementation(UI.Compose.nav_compose)
            implementation(UI.Compose.runtime)
            implementation(UI.Compose.ui_util)
            implementation(UI.Compose.activity_compose)
            implementation(UI.Compose.lifecycle_viewmodel_compose)
            implementation(UI.Compose.constraintlayout_compose)
            implementation(UI.Compose.androidx_compose_compiler)

            // UI - Material
            implementation(UI.Material.material)
            implementation(UI.Material.material3)

            // UI - Images
            implementation(UI.Images.coil)

            // UI - Navigation
            implementation(UI.Navigation.runtime)
            implementation(UI.Navigation.hilt_navigation_compose)
            implementation(UI.Navigation.ui_ktx)

            // ViewModel
            implementation(UI.ViewModel.lifecycle_viewmodel_ktx)
            implementation(UI.ViewModel.lifecycle_runtime_ktx)

            // Ktx
            implementation(AndroidX.KTX.core_ktx)

            // Google
            implementation(Google.Material.material)
            implementation(Google.Accompanist.accompanist_permissions)
            implementation(Google.Accompanist.accompanist_drawablepainter)
            implementation(Google.Accompanist.accompanist_glide)

            // DATA
            implementation(Data.Paging.common_ktx)
            implementation(Data.PrefsDataStore.datastore)
            implementation(Data.Room.runtime)
            kapt(Data.Room.compiler)
            implementation(Data.Room.ktx)
            implementation(Data.Room.paging)
            implementation(Data.Paging.compose)
            implementation(Data.Paging.runtime)
            implementation(Data.Paging.runtime_ktx)
            implementation(Data.Paging.runtime_common)

            // UTILS
            implementation(Utils.Images.image_compressor)
            //debugImplementation(Utils.Development.leakCanary)
            implementation(Utils.Uri.uri2file_utils)

            // DAGGER HILT
            implementation(Google.Hilt.android)
            implementation(UI.Navigation.hilt_navigation_compose)
            kapt(Google.Hilt.android_compiler)
            kapt(DI.Hilt.hilt_compiler)

            // PERSONAL LIBRARIES
            implementation(PersonalLibs.Utils.Currency.currency_processor)

            // Unit Tests
            testImplementation(Tests.JavaFramework.junit)
            testImplementation(Tests.JavaFramework.mockito)
            testImplementation(Tests.JavaFramework.mockito_kotlin)
            testImplementation(Tests.JavaFramework.robolectric)
            testImplementation(Tests.AndroidFrameWork.androidx_junit_ktx)
            testImplementation(Tests.AndroidFrameWork.androidx_test_core)
            testImplementation(Tests.AndroidFrameWork.androidx_test_core_ktx)
            testImplementation(Tests.Common.google_truth)

            // Instrumented Tests
            androidTestImplementation(platform(UI.Compose.bom))
            androidTestImplementation(Tests.AndroidFrameWork.androidx_compose_ui_test)
            androidTestImplementation(Tests.AndroidFrameWork.androidx_compose_ui_test_manifest)
            androidTestImplementation(Tests.AndroidFrameWork.androidx_compose_junit)
            debugImplementation(Tests.AndroidFrameWork.androidx_compose_tooling)

            androidTestImplementation(Tests.AndroidFrameWork.androidx_junit)
            androidTestImplementation(Tests.AndroidFrameWork.androidx_junit_ktx)
            androidTestImplementation(Tests.AndroidFrameWork.androidx_test_core_ktx)
            androidTestImplementation(Tests.AndroidFrameWork.androidx_test_runner)
            androidTestImplementation(Tests.AndroidFrameWork.androidx_test_orchestrator)
            androidTestImplementation(Tests.AndroidFrameWork.androidx_espresso)
            androidTestImplementation(Tests.AndroidFrameWork.androidx_truth)
            androidTestImplementation(Tests.AndroidFrameWork.nav_testing)

            androidTestImplementation(Tests.Common.google_truth)
        }
    """.trimIndent()

    val appModuleDir = File(moduleData.projectTemplateData.rootDir, "app")

    if (!appModuleDir.exists()) appModuleDir.mkdirs()

    val buildFile = File(appModuleDir, "build.gradle.kts")

    buildFile.writeText(buildFileContent)
}