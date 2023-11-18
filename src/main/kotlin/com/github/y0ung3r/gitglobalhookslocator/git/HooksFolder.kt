package com.github.y0ung3r.gitglobalhookslocator.git

import com.github.y0ung3r.gitglobalhookslocator.git.extensions.getGlobalHooksPath
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

class HooksFolder(git: Git) {
    companion object {
        @JvmStatic
        val availableHooks = arrayOf(
            "pre-commit",
            "prepare-commit-msg",
            "commit-msg",
            "post-commit",
            "applypatch-msg",
            "pre-applypatch",
            "post-applypatch",
            "pre-rebase",
            "post-rewrite",
            "post-checkout",
            "post-merge",
            "pre-push",
            "pre-auto-gc"
        )
    }

    val hooks: List<HookEntry>

    init {
        val folderPath = Path.of(git.getGlobalHooksPath())

        hooks = Files
            .list(folderPath)
            .filter {
                availableHooks
                    .any { hookName ->
                        it.fileName.nameWithoutExtension.contains(hookName) }
            }
            .map { HookEntry.load(it) }
            .toList()
    }

    fun isAllDisabled()
        = hooks.all { it.isDisabled() }

    fun enableAll()
        = hooks.forEach { it.enable() }

    fun disableAll()
        = hooks.forEach { it.disable() }
}