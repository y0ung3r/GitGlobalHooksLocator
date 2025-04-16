package com.github.y0ung3r.gitglobalhookslocator.ui.commitOptions

import com.github.y0ung3r.gitglobalhookslocator.LocatorBundle
import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.HooksFolder
import com.intellij.openapi.vcs.ui.RefreshableOnComponent
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.UIUtil
import javax.swing.JComponent

class GlobalHooksGroup : RefreshableOnComponent {
	override fun saveState()
	{ }

	override fun restoreState()
	{ }

	override fun getComponent(): JComponent {
		val hooksFolder = HooksFolder(Git.instance)

		lateinit var disableAllCheckBox: JBCheckBox
		lateinit var hookCheckBoxes: List<JBCheckBox>

		return panel {
			group(LocatorBundle.getMessage("locator.ui.group.title")) {
				if (hooksFolder.isEmpty()) {
					row {
						label(LocatorBundle.getMessage("locator.ui.group.notFound", hooksFolder.path))
							.align(Align.CENTER)
							.applyToComponent {
								foreground = UIUtil.getLabelInfoForeground()
							}
					}
				} else {
					row {
						cell(JBCheckBox(LocatorBundle.getMessage("locator.ui.group.disableAllHooks"), hooksFolder.isAllDisabled())
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

								disableAllCheckBox = this
							})
					}

					hooksFolder
						.hooks
						.map { hook ->
							JBCheckBox(LocatorBundle.getMessage("locator.ui.group.disableHook", hook.name), hook.isDisabled())
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
						.apply { hookCheckBoxes = this }
						.map {
							row {
								cell(it)
							}
						}
				}
			}
		}
	}
}