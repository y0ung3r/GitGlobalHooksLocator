package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.git.cli.EmptyCliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.GitCommandNotFoundException
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ExecuteCommandTests {
    @Test(expected = GitCommandNotFoundException::class)
    fun `Should throws exception if Git commands is not exists`() {
        // Arrange & Act & Assert
        Git.instance.executeCommand(
            "unknown",
            Git.GIT_GLOBAL_COMMAND
        )
    }

    @Test
    fun `Should returns response properly`() {
        // Arrange
        val sut = Git.instance

        // Act
        val actualResponse = sut.executeCommand(
            Git.GIT_CONFIG_COMMAND,
            Git.GIT_GLOBAL_COMMAND,
            "user.name"
        )

        // Assert
        assertNotEquals(actualResponse, EmptyCliResponse())
    }
}