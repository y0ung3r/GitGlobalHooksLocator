package com.github.y0ung3r.gitglobalhookslocator.git

import com.github.y0ung3r.gitglobalhookslocator.git.cli.DefaultCliCommandExecutor
import com.github.y0ung3r.gitglobalhookslocator.git.cli.EmptyCliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.NotFoundCliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.interfaces.CliCommandExecutor
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.GitCommandNotFoundException
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.GitIsNotInstalledException
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.GitVersionIsNotSupportedException
import com.github.y0ung3r.gitglobalhookslocator.git.extensions.toGitResponse

class Git(private val commandExecutor: CliCommandExecutor) {
    companion object {
        private const val GIT_COMMAND = "git"
        private const val GIT_VERSION_COMMAND = "version"
        const val GIT_CONFIG_COMMAND = "config"
        const val GIT_GLOBAL_COMMAND = "--global"
        const val GIT_CONFIG_GET_COMMAND = "--get"
        const val GIT_HOOKS_PATH_SECTION = "core.hooksPath"
        const val DEFAULT_HOOKS_PATH = "./.git/hooks"

        @JvmStatic
        val minRequiredVersion = SemanticVersion(2, 9, 0)

        @JvmStatic
        val instance = Git(DefaultCliCommandExecutor())
    }

    init {
        if (getInstalledVersion() < minRequiredVersion) {
            throw GitVersionIsNotSupportedException()
        }
    }

    fun executeCommand(vararg params: String): GitResponse {
        val processBuilder = ProcessBuilder(
            ArrayList<String>().apply {
                add(GIT_COMMAND)
                addAll(params)
            }
        )

        processBuilder.redirectErrorStream(true)

        return when (val response = commandExecutor.execute(processBuilder)) {
            is NotFoundCliResponse -> throw GitCommandNotFoundException(response.value, *params)
            else -> response.toGitResponse()
        }
    }

    fun getInstalledVersion() : SemanticVersion {
        val installedVersion = try {
            executeCommand(GIT_VERSION_COMMAND)
        } catch (exception: GitCommandNotFoundException) {
            GitResponse(EmptyCliResponse())
        }

        return when {
            !installedVersion.isEmpty() -> SemanticVersion.parse(installedVersion.value)
            else -> throw GitIsNotInstalledException()
        }
    }
}