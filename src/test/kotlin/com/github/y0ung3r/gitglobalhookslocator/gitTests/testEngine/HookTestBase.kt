package com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine

import com.github.y0ung3r.gitglobalhookslocator.git.HooksFolder
import org.junit.runners.Parameterized
import java.nio.file.Files
import java.nio.file.Path

abstract class HookTestBase {
    companion object {
        const val BASE_PATH = "src/test/testData/hooks"
        private const val DISABLED_HOOK = "disabled"
        private const val ENABLED_HOOK = "enabled"

        @JvmStatic
        private fun getHookPath(hookType: String, hookName: String): Path {
            val categorizedHookName = when (hookType) {
                DISABLED_HOOK -> "_$hookName"
                else -> hookName
            }

            return Path.of(BASE_PATH, hookType, categorizedHookName)
        }

        @JvmStatic
        private fun getHooksPath(hookType: String): Path
            = Path.of(BASE_PATH, hookType)

        @JvmStatic
        fun getDisabledHookPath(hookName: String): Path
            = getHookPath(DISABLED_HOOK, hookName)

        @JvmStatic
        fun getEnabledHookPath(hookName: String): Path
            = getHookPath(ENABLED_HOOK, hookName)

        @JvmStatic
        fun getDisabledHooksPath(): Path
            = getHooksPath(DISABLED_HOOK)

        @JvmStatic
        fun getEnabledHooksPath(): Path
            = getHooksPath(ENABLED_HOOK)

        @JvmStatic
        private fun clearTestHooks(hookType: String) {
            val hooksPath = getHooksPath(hookType)

            hooksPath
                .toFile()
                .mkdirs()

            Files.list(hooksPath).forEach { it.toFile().delete() }
        }

        @JvmStatic
        private fun generateTestHooks(hookType: String) {
            clearTestHooks(hookType)

            hookNames()
                .map { getHookPath(hookType, it).toFile() }
                .filter { !it.exists() }
                .forEach { it.createNewFile() }
        }

        @JvmStatic
        @Parameterized.Parameters
        fun hookNames()
            = HooksFolder.availableHooks
    }

    init {
        generateTestHooks(DISABLED_HOOK)
        generateTestHooks(ENABLED_HOOK)
    }
}