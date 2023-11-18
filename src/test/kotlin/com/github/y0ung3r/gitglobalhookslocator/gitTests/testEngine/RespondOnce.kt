package com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine

import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.interfaces.CliCommandExecutor

class RespondOnce(private val response: CliResponse) : CliCommandExecutor {
    override fun execute(processBuilder: ProcessBuilder): CliResponse
        = response
}