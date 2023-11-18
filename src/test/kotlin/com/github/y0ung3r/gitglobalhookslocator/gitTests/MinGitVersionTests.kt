package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.RespondOnce
import com.github.y0ung3r.gitglobalhookslocator.services.git.Git
import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.services.git.exceptions.GitVersionIsNotSupportedException
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MinGitVersionTests(private val unsupportedVersion: String, private val supportedVersion: String) {
    companion object {
        @JvmStatic
        @Parameters
        fun version() = arrayOf(
            arrayOf("1.0.0", "2.9.0"),
            arrayOf("1.0.1", "2.9.1"),
            arrayOf("2.0.0", "2.43.0"),
            arrayOf("2.0.1", "2.43.1"),
        )
    }

    @Test(expected = GitVersionIsNotSupportedException::class)
    fun `Should throws exception if Git version is not supported`() {
        // Arrange & Act & Assert
        Git(
            RespondOnce(
                CliResponse(unsupportedVersion)
            )
        )
    }

    @Test
    fun `Should not throws exception if Git version supported`() {
        // Arrange & Act & Assert
        Git(
            RespondOnce(
                CliResponse(supportedVersion)
            )
        )
    }
}