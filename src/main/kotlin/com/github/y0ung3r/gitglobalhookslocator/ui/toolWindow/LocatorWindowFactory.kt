package com.github.y0ung3r.gitglobalhookslocator.ui.toolWindow

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.ContentManagerListener

class LocatorWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val locatorWindow = LocatorWindow(toolWindow)

        val content = ContentFactory
            .getInstance()
            .createContent(
                locatorWindow.getContent(),
                null,
                false)

        toolWindow
            .contentManager
            .addContent(content)
    }

    override fun shouldBeAvailable(project: Project)
        = !project.isDefault
}
