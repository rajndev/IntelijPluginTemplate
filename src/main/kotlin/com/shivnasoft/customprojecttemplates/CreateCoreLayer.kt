package com.shivnasoft.customprojecttemplates

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import java.io.File

fun RecipeExecutor.createCoreModules(moduleData: ModuleTemplateData) {
    val rootDir = moduleData.projectTemplateData.rootDir
    val settingsGradleFile = File(rootDir, "settings.gradle.kts")

    val coreModules = listOf("common", "ui", "util")

    val baseNamespace = moduleData.packageName

    // Create settings.gradle.kts with base structure
    val settingsGradleBase = """
        pluginManagement {
            repositories {
                google()
                mavenCentral()
                maven(url = "https://jitpack.io")
                gradlePluginPortal()
            }
        }

        dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                google()
                mavenCentral()
                mavenLocal()
                maven(url = "https://jitpack.io")
            }
        }
     
        rootProject.name = "${moduleData.projectTemplateData.rootDir.name}"
        include(":app")

    """.trimIndent() + "\n"

    settingsGradleFile.writeText(settingsGradleBase)

    // Create each core module and append its include to settings.gradle.kts
    coreModules.forEach { module ->
        val moduleDirPath = File(rootDir, "core/$module")
        val buildFilePath = File(moduleDirPath, "build.gradle.kts")
        val namespace = "$baseNamespace.core.$module"

        // Create module directory and placeholder build.gradle.kts
        moduleDirPath.mkdirs()

        val gradleFileOutput = when(module) {
            "common" -> """
               plugins {
                   id("com.android.library")
                   id("org.jetbrains.kotlin.android")
                   id("kotlin-parcelize")
                   id("dagger.hilt.android.plugin")
                   id("kotlin-kapt")
               }

               android {
                    namespace = "$namespace"
                   compileSdk = 34

                   defaultConfig {
                       minSdk = 27

                       testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                       consumerProguardFiles("consumer-rules.pro")
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
               /*        sourceCompatibility = JavaVersion.VERSION_22
                       targetCompatibility = JavaVersion.VERSION_22*/

                       sourceCompatibility = JavaVersion.VERSION_17
                       targetCompatibility = JavaVersion.VERSION_17
                   }
                   kotlinOptions {
                       jvmTarget = "17"
                   }
               }

               dependencies {
                   //implementation(project(":domain:shopping_lists"))
                   // UI - Activity
                   implementation(UI.Activity.activity_activity)
                   implementation(UI.Activity.androidx_appcompat)

                   // UI - Compose
                   implementation(platform(UI.Compose.bom))

                   implementation(UI.Compose.ui)
                 // implementation(UI.Compose.androidx_compose_animation)
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
                   //implementation(PersonalLibs.Utils.Images.image_utils) //unauth when dl. Not sure why.

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
                   //implementation(Data.PrefsDataStore.datastore)
                   implementation(Data.Room.runtime)
                   implementation("androidx.datastore:datastore-preferences:1.0.0")
                   //implementation("androidx.datastore:datastore-preferences-core:1.0.0")
                   kapt(Data.Room.compiler)
                   implementation(Data.Room.ktx)
                   implementation(Data.Room.paging)
                   implementation(Data.Paging.compose)
                   implementation(Data.Paging.runtime)
                   implementation(Data.Paging.runtime_ktx)
                   implementation(Data.Paging.runtime_common)
                   implementation(Data.Paging.common_ktx)

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
                   //implementation(PersonalLibs.Permissions.compose_permissions)
                   //implementation(PersonalLibs.Compose.compose_components)
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
            "ui" -> """
                plugins {
                    id("com.android.library")
                    id("org.jetbrains.kotlin.android")
                    id("kotlin-parcelize")
                    id("dagger.hilt.android.plugin")
                    id("kotlin-kapt")
                }

                android {
                    namespace = "$namespace"
                    compileSdk = 34

                    defaultConfig {
                        minSdk = 27

                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                        consumerProguardFiles("consumer-rules.pro")
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
                }

                dependencies {
                    //implementation(project(":core:common"))
                    //implementation(project(":domain:shopping_lists"))
                    //implementation(project(":domain:shopping_list"))
                    //implementation(project(":domain:item_details"))

                    implementation("com.shivnasoft:compose-components:1.0.0")
                    implementation("com.shivnasoft:compose-permissions:1.0.0")

                /*    implementation(files(PersonalLibs.FilePaths.composeComponentsLibAARFilePath))
                    implementation(files(PersonalLibs.FilePaths.composePermissionsLibAARFilePath))*/

                    // UI - Activity
                    implementation(UI.Activity.activity_activity)
                    implementation(UI.Activity.androidx_appcompat)

                    // UI - Compose
                    implementation(platform(UI.Compose.bom))

                    implementation(UI.Compose.ui)
                   // implementation(UI.Compose.androidx_compose_animation)
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
                    //implementation(PersonalLibs.Utils.Images.image_utils) //unauth when dl. Not sure why.

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
                    implementation(Data.Room.runtime)/*
                    compileOnly(files("../custom-libs/compose-components.aar"))
                    compileOnly(files("../custom-libs/compose-permissions.aar"))*/
                    kapt(Data.Room.compiler)
                    implementation(Data.Room.ktx)
                    implementation(Data.Room.paging)
                    implementation(Data.Paging.compose)
                    implementation(Data.Paging.runtime)
                    implementation(Data.Paging.runtime_ktx)
                    implementation(Data.Paging.runtime_common)
                    implementation(Data.Paging.common_ktx)

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
                    //implementation(PersonalLibs.Permissions.compose_permissions)
                    //implementation(PersonalLibs.Compose.compose_components)
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
            "util" -> """
             plugins {
                 id("com.android.library")
                 id("org.jetbrains.kotlin.android")
                 id("dagger.hilt.android.plugin")
                 id("kotlin-kapt")
             }

             android {
                    namespace = "$namespace"
                 compileSdk = 34

                 defaultConfig {
                     minSdk = 27

                     testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                     consumerProguardFiles("consumer-rules.pro")
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
             }

             dependencies {
                 //implementation(project(":core:common"))

                 // UI - Activity
                 implementation(UI.Activity.activity_activity)
                 implementation(UI.Activity.androidx_appcompat)

                 // UI - Compose
                 implementation(platform(UI.Compose.bom))

                 implementation(UI.Compose.ui)
                // implementation(UI.Compose.androidx_compose_animation)
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
                // implementation(UI.Material.material3)

                 // UI - Images
                 implementation(UI.Images.coil)
                 //implementation(PersonalLibs.Utils.Images.image_utils) //unauth when dl. Not sure why.

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
                 implementation(Data.Paging.common_ktx)

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
                 //implementation(PersonalLibs.Permissions.compose_permissions)
                 //implementation(PersonalLibs.Compose.compose_components)
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
            else -> ""
        }

        save(gradleFileOutput, buildFilePath)

        // Append include and projectDir mapping to settings.gradle.kts
        val includeEntry = """
            include(":core:$module")
        """.trimIndent() + "\n"

        settingsGradleFile.appendText(includeEntry)
    }
}