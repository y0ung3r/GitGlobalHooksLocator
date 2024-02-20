package com.github.y0ung3r.gitglobalhookslocator.gitTests.hooksFolderTests

import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HookName
import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HooksFolder
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import org.junit.Test

class LoadTests : HookTestBase() {
    @Test
    fun `Should load all hooks from disabled hooks path`() {
        // Arrange
        val git = getMockGit(getDisabledHooksPath())

        // Act
        val sut = HooksFolder(git)

        // Assert
		assertEquals(HookName.supportedHooks.size, sut.count())
    }

    @Test
    fun `Should load all hooks from enabled hooks path`() {
        // Arrange
        val git = getMockGit(getEnabledHooksPath())

        // Act
        val sut = HooksFolder(git)

        // Assert
		assertEquals(HookName.supportedHooks.size, sut.count())
    }
}