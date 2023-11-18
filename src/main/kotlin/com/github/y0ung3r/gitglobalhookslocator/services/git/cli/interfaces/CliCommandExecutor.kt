package com.github.y0ung3r.gitglobalhookslocator.services.git.cli.interfaces

import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.CliResponse

interface CliCommandExecutor {
    fun execute(processBuilder: ProcessBuilder): CliResponse
}