package com.shivnasoft.customprojecttemplates

import java.io.File
import com.android.tools.idea.wizard.template.ModuleTemplateData

fun createAppManifest(moduleData: ModuleTemplateData, activityName: String ) {
    val appMainDir = File(moduleData.projectTemplateData.rootDir, "app/src/main")
    if (!appMainDir.exists()) appMainDir.mkdirs()

    val manifestFile = File(appMainDir, "AndroidManifest.xml")
    val packageName = moduleData.packageName // dynamically from wizard

    val content = """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:tools="http://schemas.android.com/tools"
            package="$packageName">

            <application
            android:name=".${moduleData.projectTemplateData.rootDir.name}"
                android:allowBackup="true"
                android:dataExtractionRules="@xml/data_extraction_rules"
                android:fullBackupContent="@xml/backup_rules"
                android:icon="@mipmap/ic_launcher"
                android:label="@string/app_name"
                android:roundIcon="@mipmap/ic_launcher_round"
                android:supportsRtl="true"
                android:theme="@style/Theme.${moduleData.projectTemplateData.rootDir.name}"
                tools:targetApi="31">

                <activity
                    android:name=".$activityName"
                    android:exported="true"
                    android:theme="@style/Theme.${moduleData.projectTemplateData.rootDir.name}">
                    <intent-filter>
                        <action android:name="android.intent.action.MAIN" />
                        <category android:name="android.intent.category.LAUNCHER" />
                    </intent-filter>
                </activity>

            </application>
        </manifest>
    """.trimIndent()

    manifestFile.writeText(content)
}