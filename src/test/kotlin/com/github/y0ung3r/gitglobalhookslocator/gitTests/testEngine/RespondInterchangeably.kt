package com.github.y0ung3r.gitglobalhookslocator.gitTests.testEngine

import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.interfaces.CliCommandExecutor

class RespondInterchangeably(vararg responses: CliResponse) : CliCommandExecutor {
    private val iterator = responses.iterator()
    override fun execute(processBuilder: ProcessBuilder): CliResponse
        = iterator.next()
}