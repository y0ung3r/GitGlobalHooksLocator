package com.github.y0ung3r.gitglobalhookslocator.git

import com.github.y0ung3r.gitglobalhookslocator.git.extensions.getGlobalHooksPath
import com.intellij.openapi.diagnostic.thisLogger
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension
import java.nio.file.NoSuchFileException

class HooksFolder(git: Git) {
    companion object {
        val supportedHooks = arrayOf(
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
    val path: Path = git.getGlobalHooksPath()

    fun isEmpty(): Boolean
        = hooks.isEmpty()

    init {
        val files = try {
            Files.list(path)
        }
        catch (exception: NoSuchFileException) {
            thisLogger()
                .info("Provided hooks path is not valid", exception)

            emptyList<Path>()
                .stream()
        }

        hooks = files
            .filter {
                supportedHooks
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