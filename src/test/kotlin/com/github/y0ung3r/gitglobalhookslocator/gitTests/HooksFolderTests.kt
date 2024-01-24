package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.HooksFolder
import com.github.y0ung3r.gitglobalhookslocator.git.ObservableHooksFolder
import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.RespondInterchangeably
import org.junit.Assert.*
import org.junit.Test
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds

class ObservableHooksFolderTests : HookTestBase() {
    private companion object {
        @JvmStatic
        fun getGit(hooksPath: Path) = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                CliResponse(hooksPath.toString())
            )
        )
    }

    @Test
    fun `Should observe changes`() {
        // Arrange
        val git = getGit(getEnabledHooksPath())

        // Act
        val sut = ObservableHooksFolder(git)

        sut.hooks.subscribe {
            assertEquals(it.kind, StandardWatchEventKinds.ENTRY_CREATE)
        }

        // Assert
        createHook("kek")
    }
}

class HooksFolderTests : HookTestBase() {
    private companion object {
        @JvmStatic
        fun getGit(hooksPath: Path) = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                CliResponse(hooksPath.toString())
            )
        )
    }

    @Test
    fun `Should load all hooks from disabled hooks path`() {
        // Arrange
        val git = getGit(getDisabledHooksPath())

        // Act
        val sut = HooksFolder(git)

        // Assert
        assertEquals(HooksFolder.supportedHooks.size, sut.hooks.size)
    }

    @Test
    fun `Should load all hooks from enabled hooks path`() {
        // Arrange
        val git = getGit(getEnabledHooksPath())

        // Act
        val sut = HooksFolder(git)

        // Assert
        assertEquals(HooksFolder.supportedHooks.size, sut.hooks.size)
    }

    @Test
    fun `All hooks should be disabled`() {
        // Arrange
        val git = getGit(getDisabledHooksPath())
        val sut = HooksFolder(git)

        // Act & Assert
        assertTrue(sut.isAllDisabled())
    }

    @Test
    fun `All hooks should be enabled`() {
        // Arrange
        val git = getGit(getEnabledHooksPath())
        val sut = HooksFolder(git)

        // Act & Assert
        assertFalse(sut.isAllDisabled())
    }

    @Test
    fun `Should disable all hooks`() {
        // Arrange
        val git = getGit(getEnabledHooksPath())
        val sut = HooksFolder(git)

        // Act
        sut.disableAll()

        // Assert
        assertTrue(sut.isAllDisabled())
    }

    @Test
    fun `Should enable all hooks`() {
        // Arrange
        val git = getGit(getDisabledHooksPath())
        val sut = HooksFolder(git)

        // Act
        sut.enableAll()

        // Assert
        assertFalse(sut.isAllDisabled())
    }
}