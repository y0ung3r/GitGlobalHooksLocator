package com.github.y0ung3r.gitglobalhookslocator.services.git

import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.services.git.cli.EmptyCliResponse

class GitResponse(private val cliResponse: CliResponse) {
    val value = cliResponse.value
    fun isEmpty() = cliResponse is EmptyCliResponse
}