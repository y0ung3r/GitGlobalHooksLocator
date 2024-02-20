package com.github.y0ung3r.gitglobalhookslocator.gitTests.hooksFolderTests

import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HookEntry
import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HookName
import com.github.y0ung3r.gitglobalhookslocator.git.hooks.HooksFolder
import com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine.HookTestBase
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ObserveTests(private val hookName: String) : HookTestBase() {
    @Test
    fun `Should observe added hooks`() {
        // Arrange
        val git = getMockGit(getCustomHooksPath())
        val sut = HooksFolder(git)
        val testObserver = TestObserver<HookEntry>()

        sut.addedHooks
            .subscribe {
                testObserver.onNext(it)
                testObserver.onComplete()
            }

        // Act
		createHook(hookName)

        // Assert
        testObserver
            .await()
            .assertValue {
                it.name.value == hookName
            }
    }

    @Test
    fun `Should observe deleted hooks`() {
        // Arrange
        val git = getMockGit(getCustomHooksPath())
        val sut = HooksFolder(git)
        val testObserver = TestObserver<HookName>()

        sut.deletedHooks
            .subscribe {
                testObserver.onNext(it)
                testObserver.onComplete()
            }

		createHook(hookName)

        // Act
		deleteHook(hookName)

        // Assert
        testObserver
            .await()
            .assertValue {
                it.value == hookName
            }
    }
}