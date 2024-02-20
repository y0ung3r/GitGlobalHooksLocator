package com.github.y0ung3r.gitglobalhookslocator.gitTests.hooksFolderTests

import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HooksFolder
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import org.junit.Test

class IsAllDisabledTests : HookTestBase() {
    @Test
    fun `All hooks should be disabled`() {
        // Arrange
        val git = getMockGit(getDisabledHooksPath())
        val sut = HooksFolder(git)

        // Act & Assert
		assertTrue(sut.isAllDisabled())
    }

    @Test
    fun `All hooks should be enabled`() {
        // Arrange
        val git = getMockGit(getEnabledHooksPath())
        val sut = HooksFolder(git)

        // Act & Assert
		assertFalse(sut.isAllDisabled())
    }
}