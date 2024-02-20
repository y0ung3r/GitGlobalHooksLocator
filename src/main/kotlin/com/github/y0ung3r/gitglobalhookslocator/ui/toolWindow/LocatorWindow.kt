package com.github.y0ung3r.gitglobalhookslocator.ui.toolWindow

import com.github.y0ung3r.gitglobalhookslocator.LocatorBundle
import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HookName
import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HooksFolder
import com.github.y0ung3r.gitglobalhookslocator.ui.Notifier
import com.github.y0ung3r.gitglobalhookslocator.utils.SystemPathUtils
import com.intellij.ide.wizard.withVisualPadding
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class LocatorWindow(private val toolWindow: ToolWindow) {
    private val hooksFolder: HooksFolder
        = HooksFolder(Git.instance)

    private lateinit var emptyHintLayout: Panel
    private lateinit var checkBoxesLayout: Panel

    private val disableAllCheckBox: JBCheckBox
        = createDisableAllCheckBox()

    private val checkBoxes: Map<String, JBCheckBox>
        = HookName
            .supportedHooks
            .associateBy({ it }, { createHookCheckBox(it) })

    fun getContent(): JComponent {
        hooksFolder
            .addedHooks
            .subscribe { hookEntry ->
                // Switching the panel visibility only when 1 item appears
                if (hooksFolder.count() == 1) {
                    checkBoxesLayout.visible(true)
                    emptyHintLayout.visible(false)
                }

                checkBoxes
                    .getValue(hookEntry.name.value)
                    .apply {
                        isVisible = true
                        isSelected = hookEntry.isDisabled()
                        isEnabled = hooksFolder.canBeUsed(hookEntry.name)

                        if (isEnabled) {
                            addActionListener {
                                when (isSelected) {
                                    true -> hookEntry.disable()
                                    false -> hookEntry.enable()
                                }

                                disableAllCheckBox.isSelected = hooksFolder.isAllDisabled()
                            }
                        }
                    }

                disableAllCheckBox.isSelected = hooksFolder.isAllDisabled()
            }

        hooksFolder
            .deletedHooks
            .subscribe { hookName ->
                disableAllCheckBox.isSelected = hooksFolder.isAllDisabled()

                checkBoxes
                    .getValue(hookName.value)
                    .apply {
                        isVisible = false
                        //isEnabled = hooksFolder.canBeUsed(hookName)

                        actionListeners.forEach {
                            removeActionListener(it)
                        }
                    }

                if (hooksFolder.isEmpty()) {
                    checkBoxesLayout.visible(false)
                    emptyHintLayout.visible(true)
                }
            }

        hooksFolder
            .renamedHooks
            .subscribe {
                checkBoxes
                    .getValue(it.name.value)
                    .apply { isSelected = it.isDisabled() }
            }

        return panel {
            val isEmpty = hooksFolder.isEmpty()

            emptyHintLayout = panel {
                row {
                    val text = LocatorBundle
                        .getMessage("locator.ui.toolWindow.hooksFolderEmpty", hooksFolder.path)

                    comment(text) {
                        if (!SystemPathUtils.tryOpen(hooksFolder.path)) {
                            val errorTitle = LocatorBundle
                                .getMessage("locator.ui.notification.title")

                            val errorMessage = LocatorBundle
                                .getMessage("locator.ui.notification.openHooksFolder.error", hooksFolder.path)

                            Notifier.error(toolWindow.project, errorTitle, errorMessage)
                        }
                    }
                    .align(Align.CENTER)
                }
            }
            .visible(isEmpty)

            checkBoxesLayout = panel {
                val groupTitle = LocatorBundle
                    .getMessage("locator.ui.toolWindow.globalHooksGroup")

                group(groupTitle) {
                    row {
                        cell(disableAllCheckBox)
                    }

                    indent {
                        checkBoxes
                            .forEach {
                                row {
                                    cell(it.value)
                                }
                            }
                    }
                }
            }
            .visible(!isEmpty)
        }
        .withVisualPadding(true)
    }

    private fun createDisableAllCheckBox(): JBCheckBox {
        val title = LocatorBundle
            .getMessage("locator.ui.toolWindow.disableAll")

        return JBCheckBox(title, hooksFolder.isAllDisabled())
            .apply {
                addActionListener {
                    when (isSelected) {
                        true -> hooksFolder.disableAll()
                        false -> hooksFolder.enableAll()
                    }

                    checkBoxes
                        .values
                        .filter { it.isVisible }
                        .forEach {
                            it.isSelected = isSelected
                        }
                }
            }
    }

    private fun createHookCheckBox(hookName: String): JBCheckBox {
        val hookTitle = LocatorBundle
            .getMessage("locator.ui.toolWindow.disable", hookName)

        return JBCheckBox(hookTitle)
            .apply {
                isVisible = false
            }
    }
}