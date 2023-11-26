package com.github.y0ung3r.gitglobalhookslocator.git.extensions

import com.github.y0ung3r.gitglobalhookslocator.git.Git
import com.github.y0ung3r.gitglobalhookslocator.git.utils.SystemPathUtils
import java.nio.file.Path

private const val SLASHES_PATTERN = "[/\\\\]"
private const val HOME_PATTERN = "^~$SLASHES_PATTERN"
private const val CURRENT_DIR_PATTERN = "^.$SLASHES_PATTERN"
private const val ROOT_PATTERN = "^$SLASHES_PATTERN"

fun Git.getGlobalHooksPath(): Path {
    val rawPath = executeCommand(
        Git.GIT_CONFIG_COMMAND,
        Git.GIT_GLOBAL_COMMAND,
        Git.GIT_CONFIG_GET_COMMAND,
        Git.GIT_HOOKS_PATH_SECTION
    )

    val targetPath = rawPath.value
        .replaceFirst(
            Regex(HOME_PATTERN),
            SystemPathUtils.getUserHomePath()
        )
        .replaceFirst(
            Regex(CURRENT_DIR_PATTERN),
            SystemPathUtils.getCurrentDirectoryPath()
        )
        .replaceFirst(
            Regex(ROOT_PATTERN),
            SystemPathUtils.getRootPath()
        )

    return Path.of(targetPath)
        .toAbsolutePath()
}