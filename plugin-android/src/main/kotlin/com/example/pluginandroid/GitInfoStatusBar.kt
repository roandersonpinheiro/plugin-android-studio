package com.example.pluginandroid


import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetProvider

class GitInfoStatusBar : com.intellij.openapi.wm.StatusBarWidgetProvider {
    override fun getWidget(project: Project): StatusBarWidget {
        return GitInfoStatusBarWidget(project)
    }
}
