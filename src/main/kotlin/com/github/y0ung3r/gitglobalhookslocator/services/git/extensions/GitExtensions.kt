package com.github.y0ung3r.gitglobalhookslocator.services.git.extensions

import com.github.y0ung3r.gitglobalhookslocator.services.git.Git

fun Git.getGlobalHooksPath(): String {
    val path = executeCommand(
        Git.GIT_CONFIG_COMMAND,
        Git.GIT_GLOBAL_COMMAND,
        Git.GIT_CONFIG_GET_COMMAND,
        Git.GIT_HOOKS_PATH_SECTION
    )

    return path.value
}