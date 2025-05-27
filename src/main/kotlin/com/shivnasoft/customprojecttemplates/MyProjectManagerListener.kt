package com.shivnasoft.customprojecttemplates

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.shivnasoft.customprojecttemplates.services.MyProjectService

internal class MyProjectManagerListener : ProjectManagerListener {

    // Call this function every time you open the project in Android Studio.
    override fun projectOpened(project: Project) {
        println("######### WellCommon Project Name : ${project.name} #########")

        // If you want a specific prefix name to work, remove the comment.
        // project.name.startsWith("Test", ignoreCase = true)
        projectInstance = project
        // }
        project.service<MyProjectService>()
    }

    // Android Studio에서 프로젝트를 닫을 때마다 해당 function 호출
    override fun projectClosing(project: Project) {
        // If you want a specific prefix name to work, remove the comment.
        // project.name.startsWith("Test", ignoreCase = true)
        projectInstance = null
        // }
        super.projectClosing(project)
    }

    companion object {
        var projectInstance: Project? = null
    }
}