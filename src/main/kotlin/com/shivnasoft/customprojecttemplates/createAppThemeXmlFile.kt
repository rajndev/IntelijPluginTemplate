package com.shivnasoft.customprojecttemplates

import java.io.File
import com.android.tools.idea.wizard.template.ModuleTemplateData

fun createAppThemesXmlFile(moduleData: ModuleTemplateData) {
    val appResValuesDir = File(moduleData.projectTemplateData.rootDir, "app/src/main/res/values")
    if (!appResValuesDir.exists()) appResValuesDir.mkdirs()

    val themesXml = File(appResValuesDir, "themes.xml")

    val content = """
        <?xml version="1.0" encoding="utf-8"?>
        <resources>

            <style name="Theme.${moduleData.projectTemplateData.rootDir.name}" parent="Theme.AppCompat.Light.NoActionBar">
                <item name="android:statusBarColor">@color/white</item>
                <item name="android:windowLightStatusBar">true</item>
            </style>

        </resources>
    """.trimIndent()

    themesXml.writeText(content)
}