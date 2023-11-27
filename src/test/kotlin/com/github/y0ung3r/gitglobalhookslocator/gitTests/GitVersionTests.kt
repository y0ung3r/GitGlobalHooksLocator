package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.git.GitVersion
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.ProvidedGitVersionIsInvalidException
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class GitVersionTests(
    private val invalidVersion: String,
    private val validVersion: String,
    private val validMajor: Int,
    private val validMinor: Int,
    private val validPatch: Int) {
    companion object {
        @JvmStatic
        @Parameters
        fun versions() = arrayOf(
            arrayOf("git version -1.0.0", "git version 1.1.1", 1, 1, 1),
            arrayOf("git version 1.-1.0", "git version 2.42.0.windows.2", 2, 42, 0),
            arrayOf("git version 0.0.-1", "git version 0.99.9n", 0, 99, 9),
            arrayOf("git version 1", "git version 2.33.0.windows.2", 2, 33, 0),
            arrayOf("git version ", "git version 1.5.6.6", 1, 5, 6),
            arrayOf("1.-1.0", "git version 2.19.6", 2, 19, 6),
            arrayOf("0.0.-1", "git version 2.27.1", 2, 27, 1),
            arrayOf("1", "git version 2.31.8", 2, 31, 8),
            arrayOf(" ", "git version 2.42.0", 2, 42, 0),
            arrayOf("", "git version 2.40.1", 2, 40, 1),
            arrayOf("git version 1.0", "git version 1.7.12.4", 1, 7, 12),
            arrayOf("git version 1.0-alpha", "git version 2.14.6-alpha.01", 2, 14, 6),
            arrayOf("git version 1.0-alpha.01", "git version 2.38.3", 2, 38, 3),
            arrayOf("1.0", "git version 1.0.13", 1, 0, 13),
            arrayOf("1.0-alpha", "git version 1.9.5", 1, 9, 5),
            arrayOf("1.0-alpha.01", "git version 2.32.7", 2, 32, 7),
            arrayOf("git version a1.0.0", "git version 2.6.0-rc0", 2, 6, 0),
            arrayOf("git version 1.a0.0", "git version 1.2.3-alpha.b.3", 1, 2, 3),
            arrayOf("git version 1.0.a0", "git version 2.3.1-alpha", 2, 3, 1)
        )
    }

    @Test(expected = ProvidedGitVersionIsInvalidException::class)
    fun `Should throws exception if provided version is not valid`() {
        // Arrange & Act & Assert
        GitVersion.parse(invalidVersion)
    }

    @Test
    fun `Should parse version properly`() {
        // Arrange & Act & Assert
        GitVersion.parse(validVersion)
    }

    @Test
    fun `Should returns version properly`() {
        // Arrange & Act
        val sut = GitVersion.parse(validVersion)

        // Assert
        sut.also {
            assertEquals(it.major, validMajor)
            assertEquals(it.minor, validMinor)
            assertEquals(it.patch, validPatch)
        }
    }

    @Test
    fun `Should returns string representation`() {
        // Arrange
        val expectedRepresentation = GitVersion(validMajor, validMinor, validPatch).toString()
        val sut = GitVersion.parse(validVersion)

        // Act
        val actualRepresentation = sut.toString()

        // Assert
        assertEquals(expectedRepresentation, actualRepresentation)
    }
}