package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.RespondInterchangeably
import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.EmptyCliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.NotFoundCliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.extensions.getGlobalHooksPath
import com.github.y0ung3r.gitglobalhookslocator.utils.SystemPathUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.file.Path

class GetGlobalHooksPathTests {
    @Test
    fun `Should returns default hooks path if path is not overridden`() {
        // Arrange
        val expectedPath = Path.of(
            SystemPathUtils.getUserHomePath(),
            ".git",
            "hooks"
        )

        val sut = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                NotFoundCliResponse()
            )
        )

        // Act
        val actualPath = sut.getGlobalHooksPath()

        // Assert
        assertEquals(expectedPath, actualPath)
    }

    @Test
    fun `Should returns default hooks path if path is empty`() {
        // Arrange
        val expectedPath = Path.of(
            SystemPathUtils.getUserHomePath(),
            ".git",
            "hooks"
        )

        val sut = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                EmptyCliResponse()
            )
        )

        // Act
        val actualPath = sut.getGlobalHooksPath()

        // Assert
        assertEquals(expectedPath, actualPath)
    }

    @Test
    fun `Should returns absolute global hooks path using relative path`() {
        // Arrange
        val relativePath = "/.git/hooks"
        val expectedPath = Path
            .of(relativePath)
            .toAbsolutePath()

        val sut = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                CliResponse(relativePath)
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