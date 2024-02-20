package com.github.y0ung3r.gitglobalhookslocator.gitTests.hooksFolderTests

import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HooksFolder
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import org.junit.Assert
import org.junit.Test

class DisableAllTests : HookTestBase() {
    @Test
    fun `Should disable all hooks`() {
        // Arrange
        val git = getMockGit(getEnabledHooksPath())
        val sut = HooksFolder(git)

        // Act
        sut.disableAll()

        // Assert
		Assert.assertTrue(sut.isAllDisabled())
    }
}