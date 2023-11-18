package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.RespondInterchangeably
import com.github.y0ung3r.gitglobalhookslocator.services.git.Git
import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.services.git.extensions.getGlobalHooksPath
import org.junit.Assert.assertEquals
import org.junit.Test

class GetGlobalHooksPathTests {
    @Test
    fun `Should returns global hooks path`() {
        // Arrange
        val expectedPath = "~/.git/hooks"
        val sut = Git(
            RespondInterchangeably(
                CliResponse(Git.minRequiredVersion.toString()),
                CliResponse(expectedPath)
            )
        )

        // Act
        val actualPath = sut.getGlobalHooksPath()

        // Assert
        assertEquals(expectedPath, actualPath)
    }
}