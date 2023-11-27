package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.RespondOnce
import com.github.y0ung3r.gitglobalhookslocator.git.cli.EmptyCliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.GitVersion
import com.github.y0ung3r.gitglobalhookslocator.git.cli.NotFoundCliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.GitIsNotInstalledException
import org.junit.Assert.assertEquals
import org.junit.Test

class GetInstalledVersionTests {
    @Test(expected = GitIsNotInstalledException::class)
    fun `Should throws exception if Git is not installed`() {
        // Arrange & Act & Assert
        Git(
            RespondOnce(
                NotFoundCliResponse()
            )
        )
    }

    @Test(expected = GitIsNotInstalledException::class)
    fun `Should throws exception if Git returns empty value`() {
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
        val expectedVersion = GitVersion(2, 9, 2)
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