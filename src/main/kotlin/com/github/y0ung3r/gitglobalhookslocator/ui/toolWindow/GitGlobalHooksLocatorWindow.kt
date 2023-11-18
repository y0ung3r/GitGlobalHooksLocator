package com.github.y0ung3r.gitglobalhookslocator.ui.toolWindow

import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBPanel
import javax.swing.JComponent

class GitGlobalHooksLocatorWindow(toolWindow: ToolWindow) {
    fun getContent(): JComponent
        = JBPanel<JBPanel<*>>()
            .apply {

            }
}