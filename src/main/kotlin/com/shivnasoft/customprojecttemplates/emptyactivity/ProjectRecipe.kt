package com.shivnasoft.customprojecttemplates.emptyactivity

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.PackageName
import com.android.tools.idea.wizard.template.RecipeExecutor
import com.android.tools.idea.wizard.template.impl.activities.common.addAllKotlinDependencies
import com.shivnasoft.customprojecttemplates.createAppManifest
import com.shivnasoft.customprojecttemplates.createAppModuleBuildFile
import com.shivnasoft.customprojecttemplates.createAppStringsXmlFile
import com.shivnasoft.customprojecttemplates.createAppThemesXmlFile
import com.shivnasoft.customprojecttemplates.createCoreModules
import com.shivnasoft.customprojecttemplates.createDomainDir
import com.shivnasoft.customprojecttemplates.createFeaturesDir
import com.shivnasoft.customprojecttemplates.createGradleWrapperProperties
import com.shivnasoft.customprojecttemplates.createHiltApplicationClass
import com.shivnasoft.customprojecttemplates.createProjectBuildFile
import java.io.File

private const val COMPOSE_BOM_VERSION = "2025.05.01"
private const val COMPOSE_KOTLIN_COMPILER_VERSION = "1.5.14"

fun RecipeExecutor.projectRecipe(
    moduleData: ModuleTemplateData,
    packageName: PackageName,
    activityName: String,
    canAddComposeDependencies: Boolean
) {
    val rootApp = File(moduleData.projectTemplateData.rootDir, "App")

    val buildSrcDir = File(moduleData.projectTemplateData.rootDir, "buildSrc")
    val buildSrcDirForFiles = File(moduleData.projectTemplateData.rootDir, "buildSrc/src/main/java")

/*    val tomlRootPath = File(moduleData.projectTemplateData.rootDir, "gradle")
    val tomlFilePath = File(tomlRootPath, "libs.versions.toml")*/

    val emptyActivity = emptyActivity(packageName, activityName)
    val emptyActivityPath = moduleData.srcDir.resolve("$activityName.kt")
    save(emptyActivity, emptyActivityPath)
    open(emptyActivityPath)

    addAllKotlinDependencies(moduleData)
    createGradleWrapperProperties(moduleData)
    createCoreModules(moduleData)

    createDirectory(buildSrcDir)

    save(
        """
            import org.gradle.kotlin.dsl.`kotlin-dsl`

            plugins {
                `kotlin-dsl`
            }

            repositories {
                mavenCentral()
            }
        """.trimIndent(),
        File("buildSrc/build.gradle.kts")
    )

    createDirectory(buildSrcDirForFiles)
    createDependencyFilesForBuildSrcDir(buildSrcDirForFiles)
    createProjectBuildFile(moduleData)
    createAppModuleBuildFile(moduleData)
    createAppThemesXmlFile(moduleData)
    createAppManifest(moduleData, activityName)
    createAppStringsXmlFile(moduleData)
    createHiltApplicationClass(moduleData)
    createDomainDir(moduleData)
    createFeaturesDir(moduleData)
/*
    if (canAddComposeDependencies) {
        //addComposeDependencies()
    }*/

    //addAllKotlinDependencies(moduleData)
    //addMaterial3Dependency()

/*
    if (tomlFilePath.exists()) {
        tomlFilePath.delete()
    }*/
}

private fun RecipeExecutor.createDependencyFilesForBuildSrcDir(buildSrcDirForFiles: File) {
    buildSrcDirForFiles.mkdirs()

    val dependencyFiles = mapOf(
        "AndroidX.kt" to DependencyContent.androidX,
        "Data.kt" to DependencyContent.data,
        "DI.kt" to DependencyContent.di,
        "Google.kt" to DependencyContent.google,
        "JetBrains.kt" to DependencyContent.jetbrains,
        "PersonalLibs.kt" to DependencyContent.personalLibs,
        "Tests.kt" to DependencyContent.tests,
        "UI.kt" to DependencyContent.ui,
        "Utils.kt" to DependencyContent.utils,
        "Versions.kt" to DependencyContent.versions
    )

    dependencyFiles.forEach { (fileName, content) ->
        save(content, File(buildSrcDirForFiles, fileName))
    }
}

private fun RecipeExecutor.addComposeDependencies() {
    addDependency("androidx.activity:activity-compose:1.5.1")
    addPlatformDependency("androidx.compose:compose-bom:$COMPOSE_BOM_VERSION")
    addPlatformDependency("androidx.compose:compose-bom:$COMPOSE_BOM_VERSION", "androidTestImplementation")
    addDependency(mavenCoordinate = "androidx.compose.ui:ui")
    addDependency(mavenCoordinate = "androidx.compose.ui:ui-graphics")
    addDependency(mavenCoordinate = "androidx.compose.ui:ui-tooling", configuration = "debugImplementation")
    addDependency(mavenCoordinate = "androidx.compose.ui:ui-tooling-preview")
    addDependency(mavenCoordinate = "androidx.compose.ui:ui-test-manifest", configuration = "debugImplementation")
    addDependency(
        mavenCoordinate = "androidx.compose.ui:ui-test-junit4",
        configuration = "androidTestImplementation"
    )
    addDependency(mavenCoordinate = "androidx.compose.material3:material3")

    requireJavaVersion("1.8", true)
    setBuildFeature("compose", true)
    // Note: kotlinCompilerVersion default is declared in TaskManager.COMPOSE_KOTLIN_COMPILER_VERSION
    setComposeOptions(kotlinCompilerExtensionVersion = COMPOSE_KOTLIN_COMPILER_VERSION)
}

private object DependencyContent {
    val androidX = """
        import Versions.coreKtx

        object AndroidX {
            object KTX {
                const val core_ktx = "androidx.core:core-ktx:${'$'}coreKtx"
            }
        }
    """.trimIndent()

    val data = """
        import Data.Versions.pagingCompose
import Data.Versions.pagingRuntime
import Data.Versions.prefDataStore
import Data.Versions.roomKtx
import Data.Versions.roomPaging
import Data.Versions.roomVersion

object Data {
    object Versions {
        //Room
        const val roomVersion = "2.6.1"
        const val roomKtx = "2.4.2"
        const val roomPaging = "2.6.1"

        //Paging
        const val pagingCompose = "1.0.0-alpha17"
        const val pagingRuntime = "3.1.1"

        //PrefsDataStore
        const val prefDataStore = "1.0.0"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${'$'}roomVersion"
        const val compiler = "androidx.room:room-compiler:${'$'}roomVersion"
        const val ktx = "androidx.room:room-ktx:${'$'}roomKtx"
        const val paging = "androidx.room:room-paging:${'$'}roomPaging"
    }

    object Paging {
        const val compose = "androidx.paging:paging-compose:${'$'}pagingCompose"
        const val runtime = "androidx.paging:paging-runtime:${'$'}pagingRuntime"
        const val common_ktx = "androidx.paging:paging-common-ktx:${'$'}pagingRuntime"
        const val runtime_ktx = "androidx.paging:paging-runtime-ktx:${'$'}pagingRuntime"
        const val runtime_common = "androidx.paging:paging-common:${'$'}pagingRuntime"
    }

    object PrefsDataStore {
        const val datastore = "androidx.datastore:datastore-preferences:${'$'}prefDataStore"
    }
}
    """.trimIndent()
    val di = """
    import DI.Versions.hiltCompiler

object DI {
    object Versions {
        const val hiltCompiler = "1.2.0"
    }

    object Hilt {
        const val hilt_compiler = "androidx.hilt:hilt-compiler:${'$'}hiltCompiler"
    }

}
  """.trimIndent()
    val google =  """
    import Google.Versions.accompanistDrawablePainter
import Google.Versions.accompanistGlide
import Google.Versions.accompanistPermissions
import Google.Versions.accompanistSystemUiController
import Google.Versions.hiltAndroidCompiler
import Google.Versions.hiltAndroidVersion
import Google.Versions.viewMaterialVersion

object Google {
    object Versions {
        //Hilt
        const val hiltAndroidVersion = "2.50"
        const val hiltAndroidCompiler = "2.50"

        //Accompanist
        const val accompanistPermissions = "0.35.0-alpha"
        const val accompanistDrawablePainter = "0.24.10-beta"
        const val accompanistGlide = "0.15.0"
        const val accompanistSystemUiController = "0.28.0"

        //Material
        const val viewMaterialVersion = "1.6.1"
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${'$'}hiltAndroidVersion"
        const val android_compiler = "com.google.dagger:hilt-android-compiler:${'$'}hiltAndroidCompiler"
    }
    object Accompanist {
        const val accompanist_permissions = "com.google.accompanist:accompanist-permissions:${'$'}accompanistPermissions"
        const val accompanist_systemuicontroller = "com.google.accompanist:accompanist-systemuicontroller:${'$'}accompanistSystemUiController"
        const val accompanist_drawablepainter = "com.google.accompanist:accompanist-drawablepainter:${'$'}accompanistDrawablePainter"
        const val accompanist_glide = "com.google.accompanist:accompanist-glide:${'$'}accompanistGlide"
    }
    object Material {
        const val material = "com.google.android.material:material:${'$'}viewMaterialVersion"
    }
}
  """.trimIndent()
    val jetbrains =  """
    import JetBrains.Versions.kotlinCollections
import JetBrains.Versions.kotlinCoroutines

object JetBrains {
    object Versions {
        const val kotlinCollections = "0.3.5"
        const val kotlinCoroutines = "1.6.0"

    }

    object collections {
        const val kotlin_collections_immutable =
            "org.jetbrains.kotlin:kotlin-collections-immutable:${'$'}kotlinCollections"
        const val kotlin_coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${'$'}kotlinCoroutines"
    }
}
  """.trimIndent()
    val personalLibs =  """
    import PersonalLibs.Versions.composeComponents
import PersonalLibs.Versions.composePermissions
import PersonalLibs.Versions.currencyProcessor
import PersonalLibs.Versions.imageUtilsVersion

object PersonalLibs {
    object FilePaths {
/*        const val composeComponentsLibAARFilePath = "D:/DevProjects/Android-Studio-Projects/Libraries/NewPersonalLibs/ComposeComponents/shivnasoft-compose-components/build/output/aar/shivnasoft-compose-components-debug.aar"

        const val composePermissionsLibAARFilePath = "D:/DevProjects/Android-Studio-Projects/Libraries/NewPersonalLibs/ComposePermissions/compose-permissions/build/outputs/aar/compose-permissions-debug.aar"*/

        const val composeComponentsLibAARFilePath = "../../libs/compose-components.aar"

        const val composePermissionsLibAARFilePath = "../../libs/compose-permissions.aar"
    }
    object Versions {
        const val composePermissions = "1.1.5"
        const val composeComponents = "1.9.0"
        const val currencyProcessor = "1.0.1"
        const val imageUtilsVersion = "1.0.2"
    }

    object Permissions {
        const val compose_permissions = "com.github.rajndev:compose-permissions:${'$'}composePermissions"
    }

    object Compose {
        const val compose_components = "com.github.rajndev:compose-components:${'$'}composeComponents"
    }

    object Utils {
        object Currency {
            const val currency_processor =
                "com.github.rajndev:currency-processor:${'$'}currencyProcessor"

        }

        object Images {
            const val image_utils = "com.github.rajndev:image-utils:${'$'}imageUtilsVersion"

        }
    }
}
  """.trimIndent()
    val tests = """
    import Tests.Versions.androidxEspressoVersion
import Tests.Versions.androidxJunitVersion
import Tests.Versions.androidxTestCoreVersion
import Tests.Versions.androidxTestOrchestrator
import Tests.Versions.androidxTestRunnerVersion
import Tests.Versions.junitVersion
import Tests.Versions.mockitoKotlinVersion
import Tests.Versions.mockitoVersion
import Tests.Versions.robolectricVersion
import Tests.Versions.truthVersion
import Versions.navigation_version

object Tests {
    object Versions {
        //Android
        const val androidxTestCoreVersion = "1.5.0"
        const val androidxTestCoreKtxVersion = "1.5.0"
        const val androidxJunitVersion = "1.1.5"
        const val androidxJunitKtxVersion= "1.1.5"
        const val androidxEspressoVersion = "3.5.1"
        const val androidxTruthVersion = "1.5.0"
        const val androidxTestRunnerVersion = "1.5.2"
        const val androidxTestOrchestrator = "1.4.2"

        //Java
        const val mockitoVersion = "5.12.0"
        const val mockitoKotlinVersion = "5.3.1"
        const val junitVersion = "4.13.2"
        const val robolectricVersion = "4.12.2"

        //Common
        const val truthVersion = "1.4.2"
    }

    object AndroidFrameWork {
        //Compose
        const val androidx_compose_junit = "androidx.compose.ui:ui-test-junit4"
        const val androidx_compose_ui_test = "androidx.compose.ui:ui-test"
        const val androidx_compose_tooling = "androidx.compose.ui:ui-tooling"
        const val androidx_compose_ui_test_manifest = "androidx.compose.ui:ui-test-manifest"

        //JUnit
        const val androidx_junit = "androidx.test.ext:junit:${'$'}androidxJunitVersion"
        const val androidx_junit_ktx = "androidx.test.ext:junit-ktx:${'$'}androidxJunitVersion"

        //Core
        const val androidx_test_core = "androidx.test:core:${'$'}androidxTestCoreVersion"
        const val androidx_test_core_ktx = "androidx.test:core-ktx:${'$'}androidxTestCoreVersion"

        //Expresso
        const val androidx_espresso = "androidx.test.espresso:espresso-core:${'$'}androidxEspressoVersion"

        //Nav
        const val nav_testing = "androidx.navigation:navigation-testing:${'$'}navigation_version"

        //Truth
        const val androidx_truth = "androidx.test.ext:truth:${'$'}androidxTestCoreVersion"

        //Test Runner
        const val androidx_test_runner = "androidx.test:runner:${'$'}androidxTestRunnerVersion"

        //Orchestrator
        const val androidx_test_orchestrator = "androidx.test:orchestrator:${'$'}androidxTestOrchestrator"
    }

    object JavaFramework {
        const val junit = "junit:junit:${'$'}junitVersion"
        const val mockito = "org.mockito:mockito-core:${'$'}mockitoVersion"
        const val mockito_kotlin = "org.mockito.kotlin:mockito-kotlin:${'$'}mockitoKotlinVersion"
        const val robolectric = "org.robolectric:robolectric:${'$'}robolectricVersion"
    }

    object Common {
        //Truth
        const val google_truth = "com.google.truth:truth:${'$'}truthVersion"
    }
}

  """.trimIndent()
    val ui = """
    import UI.Versions.activity
import UI.Versions.activityCompose
import UI.Versions.appcompat
import UI.Versions.coilVersion
import UI.Versions.composeAnimation
import UI.Versions.composeCompiler
import UI.Versions.constraintLayoutCompose
import UI.Versions.hiltNavCompose
import UI.Versions.lifeCycleViewModelCompose
import UI.Versions.lifecycleRuntime
import UI.Versions.lifecycleRuntimeKtx
import Versions.navigation_version

object UI {
    object Versions {
        //Activity
        const val activity = "1.7.2"
        const val appcompat = "1.5.1"

        //Compose
        const val activityCompose = "1.7.2"
        const val lifeCycleViewModelCompose = "1.4.0"
        const val constraintLayoutCompose = "1.0.1"
        const val composeCompiler = "1.5.14"

        //Animation
        const val composeAnimation = "1.6.8"

        //Images
        const val coilVersion = "2.1.0"

        //ViewModel
        const val lifecycleRuntimeKtx = "2.4.1"
        const val lifecycleRuntime = "2.3.1"

        //Navigation
        const val hiltNavCompose = "1.2.0"
    }

    object Activity {
        const val activity_activity = "androidx.activity:activity:${'$'}activity"
        const val androidx_appcompat = "androidx.appcompat:appcompat:${'$'}appcompat"
    }
    object Compose {
        const val bom = "androidx.compose:compose-bom:2024.02.00"
        const val ui = "androidx.compose.ui:ui"
        const val ui_tooling_preview = "androidx.compose.ui:ui-tooling-preview"
        const val runtime_livedata =  "androidx.compose.runtime:runtime-livedata"
        const val nav_compose = "androidx.navigation:navigation-compose"
        const val runtime = "androidx.compose.runtime:runtime"
        const val ui_util = "androidx.compose.ui:ui-util"
        const val activity_compose = "androidx.activity:activity-compose:${'$'}activityCompose"
        const val lifecycle_viewmodel_compose =  "androidx.lifecycle:lifecycle-viewmodel-compose:${'$'}lifeCycleViewModelCompose"
        const val constraintlayout_compose =  "androidx.constraintlayout:constraintlayout-compose:${'$'}constraintLayoutCompose"
        const val androidx_compose_compiler = "androidx.compose.compiler:compiler:${'$'}composeCompiler"
        const val androidx_compose_animation = "androidx.compose.animation:animation"
    }

    object Material {
        const val material = "androidx.compose.material:material"
        //Todo: chnage the version value here to reflect latest version of material3
        const val material3 = "androidx.compose.material3:material3:1.3.0-beta03"
        const val material_icons_core = "androidx.compose.material:material-icons-core"
        const val material_icons_extended = "androidx.compose.material:material-icons-extended"
    }
    object Images {
        const val coil = "io.coil-kt:coil-compose:${'$'}coilVersion"
    }

    object Navigation {
        const val runtime = "androidx.navigation:navigation-runtime:${'$'}navigation_version"
        const val hilt_navigation_compose = "androidx.hilt:hilt-navigation-compose:${'$'}hiltNavCompose"
        const val ui_ktx = "androidx.navigation:navigation-ui-ktx:${'$'}navigation_version"
    }

    object ViewModel {
        const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${'$'}lifecycleRuntime"
        const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${'$'}lifecycleRuntimeKtx"
    }

}
  """.trimIndent()
    val utils = """
    import Utils.Versions.compressorVersion
//import Utils.Versions.leakcanary
import Utils.Versions.uri2FileUtilsVersion

object Utils {
    object Versions {
        const val uri2FileUtilsVersion = "1.31.0"
        const val compressorVersion = "3.0.1"
        //const val leakcanary = "2.9.1"
    }

    object Images {
        const val image_compressor = "id.zelory:compressor:${'$'}compressorVersion"

    }

    object Uri {
        const val uri2file_utils = "com.blankj:utilcodex:${'$'}uri2FileUtilsVersion"
    }

    object Development {
        //const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${'$'}leakcanary"
    }
}
  """.trimIndent()
    val versions = """
    object Versions {
  //Shared Versions
  const val navigation_version = "2.7.7"
  const val coreKtx = "1.9.0"

  //Gradle
  const val compileSdkVersion = 34
  const val minSdkVersion = 27
  const val targetSdkVersion = 34
  const val versionCode = 1
  const val versionName = "1.0"
  const val kotlinGradlePlugin = "1.9.24"
}
  """.trimIndent()
}
