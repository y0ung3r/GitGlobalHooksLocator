package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.RespondInterchangeably
import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.HooksFolder
import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Path

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
        assertEquals(HooksFolder.availableHooks.size, sut.hooks.size)
    }

    @Test
    fun `Should load all hooks from enabled hooks path`() {
        // Arrange
        val git = getGit(getEnabledHooksPath())

        // Act
        val sut = HooksFolder(git)

        // Assert
        assertEquals(HooksFolder.availableHooks.size, sut.hooks.size)
    }

    @Test
    fun `All hooks should be disabled`() {
        // Arrange
        val git = getGit(getDisabledHooksPath())
        val sut = HooksFolder(git)

        // Act & Assert
        Assert.assertTrue(sut.isAllDisabled())
    }

    @Test
    fun `All hooks should be enabled`() {
        // Arrange
        val git = getGit(getEnabledHooksPath())
        val sut = HooksFolder(git)

        // Act & Assert
        Assert.assertFalse(sut.isAllDisabled())
    }

    @Test
    fun `Should disable all hooks`() {
        // Arrange
        val git = getGit(getEnabledHooksPath())
        val sut = HooksFolder(git)

        // Act
        sut.disableAll()

        // Assert
        Assert.assertTrue(sut.isAllDisabled())
    }

    @Test
    fun `Should enable all hooks`() {
        // Arrange
        val git = getGit(getDisabledHooksPath())
        val sut = HooksFolder(git)

        // Act
        sut.enableAll()

        // Assert
        Assert.assertFalse(sut.isAllDisabled())
    }
}