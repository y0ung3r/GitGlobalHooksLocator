package com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine

import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.interfaces.CliCommandExecutor

class RespondOnce(private val response: CliResponse) : CliCommandExecutor {
    override fun execute(processBuilder: ProcessBuilder): CliResponse
        = response
}