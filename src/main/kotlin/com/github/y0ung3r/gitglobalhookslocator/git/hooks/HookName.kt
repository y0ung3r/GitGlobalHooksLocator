package com.github.y0ung3r.gitglobalhookslocator.git.hooks

import com.github.y0ung3r.gitglobalhookslocator.git.hooks.exceptions.UnsupportedHookNameException
import gnu.trove.Equality
import java.io.File
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

class HookName private constructor(private val source: String) : Comparable<HookName> {
    companion object {
        @JvmStatic
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

        @JvmStatic
        fun isSupportedHook(hookPath: Path): Boolean
            = supportedHooks.any {
                hookPath.nameWithoutExtension.contains(it) }

        @JvmStatic
        fun create(file: File): HookName
            = HookName(file.nameWithoutExtension)

        @JvmStatic
        fun create(path: Path): HookName
            = HookName(path.nameWithoutExtension)
    }

    val value: String
        = supportedHooks
            .firstOrNull { source.contains(it) }
                ?: throw UnsupportedHookNameException(source)

    override fun compareTo(other: HookName): Int
        = other.value.compareTo(value)

    override fun equals(other: Any?): Boolean {
        val name = other as? HookName

        return when {
            name == null -> false
            compareTo(name) == 0 -> true
            else -> false
        }
    }

    override fun hashCode(): Int
        = value.hashCode()
}