package com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine

import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HookName
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.runners.Parameterized
import java.nio.file.Files
import java.nio.file.Path

abstract class HookTestBase: Assert() {
    companion object {
        private const val BASE_PATH = "src/test/testData/hooks"
        private const val DISABLED_HOOK = "disabled"
        private const val ENABLED_HOOK = "enabled"
        private const val CUSTOM_HOOK = "custom"

        @JvmStatic
        @Parameterized.Parameters
        fun hookNames()
            = HookName.supportedHooks

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
    }

    fun getDisabledHookPath(hookName: String): Path
        = getHookPath(DISABLED_HOOK, hookName)

    fun getEnabledHookPath(hookName: String): Path
        = getHookPath(ENABLED_HOOK, hookName)

    fun getCustomHookPath(hookName: String): Path
        = getHookPath(CUSTOM_HOOK, hookName)

    fun getDisabledHooksPath(): Path
        = getHooksPath(DISABLED_HOOK)

    fun getEnabledHooksPath(): Path
        = getHooksPath(ENABLED_HOOK)

    fun getCustomHooksPath(): Path
        = getHooksPath(CUSTOM_HOOK)

    fun createHook(hookName: String) {
        val hookPath = getHookPath(CUSTOM_HOOK, hookName).toFile()

        if (hookPath.exists()) {
            return
        }

        hookPath.createNewFile()
    }

    fun deleteHook(hookName: String) {
        val hookPath = getHookPath(CUSTOM_HOOK, hookName).toFile()

        if (!hookPath.exists()) {
            return
        }

        hookPath.delete()
    }

    fun getMockGit(hooksPath: Path) = Git(
        RespondInterchangeably(
            CliResponse(Git.minRequiredVersion.toString()),
            CliResponse(hooksPath.toString())
        )
    )

    @Before
    fun setUp() {
        generateTestHooks(DISABLED_HOOK)
        generateTestHooks(ENABLED_HOOK)
        clearTestHooks(CUSTOM_HOOK)
    }

    @After
    fun tearDown() {
        clearTestHooks(DISABLED_HOOK)
        clearTestHooks(ENABLED_HOOK)
        clearTestHooks(CUSTOM_HOOK)
    }
}