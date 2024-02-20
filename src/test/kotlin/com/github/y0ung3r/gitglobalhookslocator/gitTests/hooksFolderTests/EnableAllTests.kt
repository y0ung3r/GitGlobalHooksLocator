package com.github.y0ung3r.gitglobalhookslocator.gitTests.hooksFolderTests

import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HooksFolder
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import org.junit.Test

class EnableAllTests : HookTestBase() {
    @Test
    fun `Should enable all hooks`() {
        // Arrange
        val git = getMockGit(getDisabledHooksPath())
        val sut = HooksFolder(git)

        // Act
        sut.enableAll()

        // Assert
        assertFalse(sut.isAllDisabled())
    }
}