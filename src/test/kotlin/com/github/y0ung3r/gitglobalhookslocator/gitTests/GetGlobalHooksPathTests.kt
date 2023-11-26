package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.RespondInterchangeably
import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.extensions.getGlobalHooksPath
import com.github.y0ung3r.gitglobalhookslocator.git.utils.SystemPathUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Path

class GetGlobalHooksPathTests {
    @Test
    fun `Should returns absolute global hooks path using absolute path`() {
        // Arrange
        val expectedPath = "C:/Users/user/.git/hooks"
        val sut = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                CliResponse(expectedPath)
            )
        )

        // Act
        val actualPath = sut.getGlobalHooksPath()

        // Assert
        assertEquals(Path.of(expectedPath), actualPath)
    }

    @Test
    fun `Should returns absolute global hooks path using relative path`() {
        // Arrange
        val expectedPath = Path.of(
            SystemPathUtils.getCurrentDirectoryPath(),
            ".git",
            "hooks"
        )

        val sut = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                CliResponse(".git/hooks")
            )
        )

        // Act
        val actualPath = sut.getGlobalHooksPath()

        // Assert
        assertEquals(expectedPath, actualPath)
    }

    @Test
    fun `Should resolve path using $HOME variable`() {
        // Arrange
        val expectedPath = Path.of(
            SystemPathUtils.getUserHomePath(),
            ".git",
            "hooks"
        )

        val sut = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                CliResponse("~/.git/hooks")
            )
        )

        // Act
        val actualPath = sut.getGlobalHooksPath()

        // Assert
        assertEquals(expectedPath, actualPath)
    }

    @Test
    fun `Should resolve path using $CURRENTDIR variable`() {
        // Arrange
        val expectedPath = Path.of(
            SystemPathUtils.getCurrentDirectoryPath(),
            ".git",
            "hooks"
        )

        val sut = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                CliResponse("./.git/hooks")
            )
        )

        // Act
        val actualPath = sut.getGlobalHooksPath()

        // Assert
        assertEquals(expectedPath, actualPath)
    }
}