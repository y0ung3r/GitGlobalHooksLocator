package com.github.y0ung3r.gitglobalhookslocator.ui.commitOptions

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ui.CommitOptionsDialogExtension
import com.intellij.openapi.vcs.ui.RefreshableOnComponent

class GlobalHooksCommitOptions : CommitOptionsDialogExtension {
	override fun getOptions(project: Project): Collection<RefreshableOnComponent>
		= listOf(GlobalHooksGroup())
}