package com.github.y0ung3r.gitglobalhookslocator.git.extensions

import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.GitResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.exceptions.GitCommandNotFoundException
import com.github.y0ung3r.gitglobalhookslocator.git.utils.SystemPathUtils
import java.nio.file.Path

private const val SLASHES_PATTERN = "[/\\\\]"
private const val EMPTY_PATTERN = "^$"
private const val HOME_PATTERN = "^~$SLASHES_PATTERN"
private const val CURRENT_DIR_PATTERN = "^.$SLASHES_PATTERN"

fun Git.getGlobalHooksPath(): Path {
    val rawPath = try {
        executeCommand(
            Git.GIT_CONFIG_COMMAND,
            Git.GIT_GLOBAL_COMMAND,
            Git.GIT_CONFIG_GET_COMMAND,
            Git.GIT_HOOKS_PATH_SECTION
        )
    } catch (exception: GitCommandNotFoundException) {
        GitResponse(CliResponse(Git.DEFAULT_HOOKS_PATH))
    }

    val targetPath = rawPath.value
        .replaceFirst(
            Regex(EMPTY_PATTERN),
            Git.DEFAULT_HOOKS_PATH
        )
        .replaceFirst(
            Regex(HOME_PATTERN),
            SystemPathUtils.getUserHomePath()
        )
        .replaceFirst(
            Regex(CURRENT_DIR_PATTERN),
            SystemPathUtils.getCurrentDirectoryPath()
        )

    return Path
        .of(targetPath)
        .toAbsolutePath()
}