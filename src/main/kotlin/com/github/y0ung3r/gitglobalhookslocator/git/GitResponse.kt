package com.github.y0ung3r.gitglobalhookslocator.git

import com.github.y0ung3r.gitglobalhookslocator.git.cli.CliResponse
import com.github.y0ung3r.gitglobalhookslocator.git.cli.EmptyCliResponse

class GitResponse(private val cliResponse: CliResponse) {
    val value = cliResponse.value
    fun isEmpty() = cliResponse is EmptyCliResponse
}