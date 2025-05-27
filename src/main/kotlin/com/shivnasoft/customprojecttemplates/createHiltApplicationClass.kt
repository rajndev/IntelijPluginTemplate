package com.shivnasoft.customprojecttemplates

import com.android.tools.idea.wizard.template.ModuleTemplateData
import com.android.tools.idea.wizard.template.RecipeExecutor
import java.io.File

fun RecipeExecutor.createHiltApplicationClass(moduleData: ModuleTemplateData) {
    val appPackage = moduleData.packageName
    val appDir = File(moduleData.srcDir.absolutePath)
    val appFile = File(appDir, "${moduleData.projectTemplateData.rootDir.name}.kt")

    val content = """
        package $appPackage

        import android.app.Application
        import android.content.Context
        import dagger.hilt.android.HiltAndroidApp

        @HiltAndroidApp
        class ${moduleData.projectTemplateData.rootDir.name} : Application() {
            init { app = this }
            
            companion object {
                private lateinit var app: ${moduleData.projectTemplateData.rootDir.name}
                fun getAppContext(): Context = app.applicationContext
            }

            override fun onCreate() {
                super.onCreate()
            }
   }
    """.trimIndent()

    save(content, appFile)
}