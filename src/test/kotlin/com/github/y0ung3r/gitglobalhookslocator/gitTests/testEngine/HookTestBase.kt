package com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine

import com.github.y0ung3r.gitglobalhookslocator.git.HooksFolder
import java.nio.file.Files
import java.nio.file.Path
import org.junit.runners.Parameterized.Parameters

abstract class HookTestBase {
    companion object {
        private const val BASE_PATH = "src/test/testData/hooks"
        private const val DISABLED_HOOK = "disabled"
        private const val ENABLED_HOOK = "enabled"

        private fun getHookPath(hookType: String, hookName: String): Path {
            val categorizedHookName = when (hookType) {
                DISABLED_HOOK -> "_$hookName"
                else -> hookName
            }

            return Path.of(BASE_PATH, hookType, categorizedHookName)
        }

        private fun getHooksPath(hookType: String): Path
            = Path.of(BASE_PATH, hookType)

        fun getDisabledHookPath(hookName: String): Path
            = getHookPath(DISABLED_HOOK, hookName)

        fun getEnabledHookPath(hookName: String): Path
            = getHookPath(ENABLED_HOOK, hookName)

        fun getDisabledHooksPath(): Path
            = getHooksPath(DISABLED_HOOK)

        fun getEnabledHooksPath(): Path
            = getHooksPath(ENABLED_HOOK)

        private fun clearTestHooks(hookType: String) {
            val hooksPath = getHooksPath(hookType)

            hooksPath
                .toFile()
                .mkdirs()

            Files.list(hooksPath).forEach { it.toFile().delete() }
        }

        private fun generateTestHooks(hookType: String) {
            clearTestHooks(hookType)

            hookNames()
                .map { getHookPath(hookType, it).toFile() }
                .filter { !it.exists() }
                .forEach { it.createNewFile() }
        }

        @JvmStatic
        @Parameters
        fun hookNames()
            = HooksFolder.supportedHooks
    }

    init {
        generateTestHooks(DISABLED_HOOK)
        generateTestHooks(ENABLED_HOOK)
    }
}