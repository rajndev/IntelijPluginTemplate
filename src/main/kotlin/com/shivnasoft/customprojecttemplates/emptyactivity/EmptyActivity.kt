package com.shivnasoft.customprojecttemplates.emptyactivity

import com.android.tools.idea.wizard.template.escapeKotlinIdentifier

fun emptyActivity(
    packageName: String,
    activityClass: String
) = """
package ${escapeKotlinIdentifier(packageName)}

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class $activityClass : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           
        }
    }
}
"""