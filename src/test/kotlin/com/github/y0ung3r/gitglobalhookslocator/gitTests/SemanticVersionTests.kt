package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.git.SemanticVersion
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.ProvidedSemanticVersionIsInvalidException
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runners.Parameterized.Parameters

// TODO: Use parameterized
class SemanticVersionTests(private val invalidVersion: String, private val validVersion: String) {
    companion object {
        @JvmStatic
        @Parameters
        fun versions() = arrayOf(
            arrayOf("-1.0.0", "0.0.0"),
            arrayOf("1.-1.0", "1.2.3-alpha.1+build"),
            arrayOf("0.0.-1", "1.0"),
            arrayOf("1", "git version 2.33.0.windows.2"),
            arrayOf("", "0.99.9n"),
            arrayOf("1.0", "1.7.12.4"),
            arrayOf("1.0-alpha", "v2.43.0-rc1"),
            arrayOf("1.0-alpha.01", "2.38.3"),
            arrayOf("a1.0.0", "v2.6.0-rc0"),
            arrayOf("1.a0.0", "1.2.3-alpha.b.3"),
            arrayOf("1.0.a0", "2.3.1-alpha"),
            arrayOf("92233720368547758072", ""),
            arrayOf("92233720368547758072.0.0", ""),
            arrayOf("0.92233720368547758072.0", ""),
            arrayOf("0.0.92233720368547758072", "")
        )
    }

    @Test(expected = ProvidedSemanticVersionIsInvalidException::class)
    fun `Should throws exception if provided version is not valid`() {
        // Arrange & Act & Assert
        SemanticVersion.parse(invalidVersion)
    }

    @Test
    fun `Should parse version properly`() {
        // Arrange & Act & Assert
        SemanticVersion.parse(validVersion)
    }

    @Test
    fun `Should returns string representation of version`() {
        // Arrange
        val sut = SemanticVersion.parse(validVersion)

        // Act
        val actualRepresentation = sut.toString()

        // Assert
        assertEquals(validVersion, actualRepresentation)
    }
}