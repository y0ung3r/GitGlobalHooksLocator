package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HookEntry
import com.github.y0ung3r.gitglobalhookslocator.git.hooks.exceptions.HookNotFoundException
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class HookEntryTests(private val hookName: String) : HookTestBase() {
    @Test(expected = HookNotFoundException::class)
    fun `Should throws exception if hook not found`() {
        HookEntry.load(getEnabledHookPath("unknown"))
    }

    @Test
    fun `Should load hook properly`() {
        HookEntry.load(getEnabledHookPath(hookName))
    }

    @Test
    fun `Hook should be disabled`() {
        // Arrange
        val sut = HookEntry.load(getDisabledHookPath(hookName))

        // Act & Assert
        assertTrue(sut.isDisabled())
    }

    @Test
    fun `Hook should be enabled`() {
        // Arrange
        val sut = HookEntry.load(getEnabledHookPath(hookName))

        // Act & Assert
        assertFalse(sut.isDisabled())
    }

    @Test
    fun `Should disable hook`() {
        // Arrange
        val sut = HookEntry.load(getEnabledHookPath(hookName))

        // Act
        sut.disable()

        // Assert
        assertTrue(sut.isDisabled())
    }

    @Test
    fun `Should enable hook`() {
        // Arrange
        val sut = HookEntry.load(getDisabledHookPath(hookName))

        // Act
        sut.enable()

        // Assert
        assertFalse(sut.isDisabled())
    }
}