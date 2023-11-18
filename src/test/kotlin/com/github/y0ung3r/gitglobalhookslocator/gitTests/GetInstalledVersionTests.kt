package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.RespondOnce
import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.EmptyCliResponse
import com.github.y0ung3r.gitglobalhookslocator.services.git.Git
import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.services.git.SemanticVersion
import com.github.y0ung3r.gitglobalhookslocator.services.git.exceptions.GitIsNotInstalledException
import org.junit.Assert.assertEquals
import org.junit.Test

class GetInstalledVersionTests {
    @Test(expected = GitIsNotInstalledException::class)
    fun `Should throws exception if Git is not installed`() {
        // Arrange & Act & Assert
        Git(
            RespondOnce(
                EmptyCliResponse()
            )
        )
    }

    @Test
    fun `Should returns Git version info`() {
        // Arrange
        val expectedVersion = SemanticVersion(2, 9, 2)
        val sut = Git(
            RespondOnce(
                CliResponse(expectedVersion.toString())
            )
        )

        // Act
        val actualVersion = sut.getInstalledVersion()

        // Assert
        assertEquals(expectedVersion, actualVersion)
    }
}