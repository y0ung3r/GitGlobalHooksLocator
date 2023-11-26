package com.github.y0ung3r.gitglobalhookslocator.ui.toolWindow

import com.github.y0ung3r.gitglobalhookslocator.LocatorBundle
import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.HookEntry
import com.github.y0ung3r.gitglobalhookslocator.git.HooksFolder
import com.intellij.ide.wizard.withVisualPadding
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class GitGlobalHooksLocatorWindow(toolWindow: ToolWindow) {
    private val hooksFolder: HooksFolder
        = HooksFolder(Git.instance)

    private val disableAllCheckBox: JBCheckBox
        = createDisableAllCheckBox()

    private val hookCheckBoxes: Array<JBCheckBox>
        = createHookCheckBoxes()

    fun getContent(): JComponent {
        if (hooksFolder.isEmpty()) {
            return panel {
                row {
                    val text = LocatorBundle
                        .getMessage("locator.ui.toolWindow.hooksFolderEmpty", hooksFolder.path)

                    label(text)
                        .align(Align.CENTER)
                }
            }
        }

        return panel {
            val groupTitle = LocatorBundle
                .getMessage("locator.ui.toolWindow.globalHooksGroup")

            group(groupTitle) {
                row {
                    cell(disableAllCheckBox)
                }

                hookCheckBoxes
                    .map {
                        row {
                            cell(it)
                        }
                    }
            }
        }
        .withVisualPadding(true)
    }

    private fun createDisableAllCheckBox(): JBCheckBox {
        val title = LocatorBundle
            .getMessage("locator.ui.toolWindow.disableAll")

        return JBCheckBox(title, hooksFolder.isAllDisabled())
            .apply {
                addActionListener {
                    when (this.isSelected) {
                        true -> hooksFolder.disableAll()
                        false -> hooksFolder.enableAll()
                    }

                    hookCheckBoxes.forEach {
                        it.isSelected = this.isSelected
                    }
                }
            }
    }

    private fun createHookCheckBox(hook: HookEntry): JBCheckBox {
        val hookTitle = LocatorBundle
            .getMessage("locator.ui.toolWindow.disable", hook.name)

        return JBCheckBox(hookTitle, hook.isDisabled())
             .apply {
                addActionListener {
                    when (this.isSelected) {
                        true -> hook.disable()
                        false -> hook.enable()
                    }

                    disableAllCheckBox.isSelected = hooksFolder.isAllDisabled()
                }
             }
    }

    private fun createHookCheckBoxes(): Array<JBCheckBox>
        = hooksFolder
            .hooks
            .map { createHookCheckBox(it) }
            .toTypedArray()
}