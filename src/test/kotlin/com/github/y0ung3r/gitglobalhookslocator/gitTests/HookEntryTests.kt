package com.github.y0ung3r.gitglobalhookslocator.gitTests

import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import com.github.y0ung3r.gitglobalhookslocator.git.HookEntry
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.HookNotFoundException
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
    fun `Should be disabled if hook name not contains in available Git hooks`() { // TODO: Rename test
        // Arrange
        val sut = HookEntry.load(getDisabledHookPath(hookName))

        // Act & Assert
        assertTrue(sut.isDisabled())
    }

    @Test
    fun `Should be enabled if hook name contains in available Git hooks`() { // TODO: Rename test
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